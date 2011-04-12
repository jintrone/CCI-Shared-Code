package edu.mit.cci.simulation.excel.responsesurfaces;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * User: jintrone
 * Date: 2/24/11
 * Time: 11:02 PM
 * <p/>
 * A concrete response surface engine for generating response surfaces from published scenarios
 * from a variety of integrated assessment models.
 * <p/>
 * It is presumed that adjacent datasets are defined for various levels of emissions or atmospheric carbon
 * at each year - this is the "scenarios" matrix. The response surface will be evaluated at each year (an Integer) between adjacent
 * datasets.  The "output" matrix captures GDP.
 */

public class IAMResponseSurfaceEngine implements ResponseSurfaceEngine<Float, Integer> {

    /**
     * @param cols        Years for which scenarios are evaluated.
     * @param baselineIdx The baseline year against which emissions input is compared to
     * @param scenarios   Each row is a scenario of emissions values over year
     * @param output      Each row is a the GDP output / year
     * @return the response surface
     */
    public SimpleResponseSurface<Float, Integer> generateResponseSurface(Integer baselineIdx, Integer[] cols, DoubleMatrix2D scenarios, DoubleMatrix2D output) {
        if (output.rows() != scenarios.rows() || output.columns() != scenarios.columns() || scenarios.columns() != cols.length) {
            throw new IllegalArgumentException("Rows and / or cols do not match matrix dimensions");
        }

         //baselineIdx is the actual year; need to get the column index
        //baseyear must be in the past
        //so response surface is for data after baseyear column
        int baselineCol = 0;
        if (baselineIdx != null) {
            boolean found = false;
            for (int col : cols) {
                if (col == baselineIdx) {
                    found = true;
                    break;
                }
                baselineCol++;
            }
            if (!found) throw new IllegalArgumentException("Baseline year not found in columns");
        }


        //Yang's code - to be examined

        SimpleResponseSurface<Float, Integer> rs = new SimpleResponseSurface<Float, Integer>();
        //make a bunch of little matrixes starting with year after the baseyear
        DoubleMatrix2D[] pointsInYear = new DoubleMatrix2D[scenarios.columns()];

        //generate pce = percent change emissions compared to baseline emissions
        //DenseDoubleMatrix2D pce = new DenseDoubleMatrix2D(scenarios.rows(),scenarios.columns());
        //generate pgdp = percent change in gdp vs reference scenario
        //DenseDoubleMatrix2D pgdp = new DenseDoubleMatrix2D(scenarios.rows(),scenarios.columns());

        //ns = number of scenarios
        //for each column after the baseyear
        for (int nyr = baselineCol + 1; nyr < scenarios.columns(); nyr++) {
            //make new matrix: scenario index, pce, pgdp, m, b
            pointsInYear[nyr] = new DenseDoubleMatrix2D(scenarios.rows(), 5);

            //for each row
            for (int ns = 0; ns < scenarios.rows(); ns++) {
                pointsInYear[nyr].set(ns, 0, ns);
                double ec = scenarios.get(ns, nyr) / scenarios.get(0, baselineCol) - 1;
                pointsInYear[nyr].set(ns, 1, ec);
                double gdpc = output.get(ns, nyr) / output.get(0, nyr) - 1;
                pointsInYear[nyr].set(ns, 2, gdpc);
                //System.out.println("nyr = " + nyr+ "\n ns= " + ns + "\n ec= " +ec +"\n gdpc= " +gdpc);
            }
            //put in descending order

            DoubleMatrix2D m1 = pointsInYear[nyr].viewSorted(1);
            pointsInYear[nyr] = m1;

            DoubleMatrix2D m2 = pointsInYear[nyr].viewRowFlip();
            pointsInYear[nyr] = m2;

            //interpolate
            //m = (y2-y1)/(x2-x1)
            //b = y2-m*x2
            //generate m and b
            for (int ns = 0; ns < scenarios.rows() - 1; ns++) {

                double x2 = pointsInYear[nyr].get(ns + 1, 1);
                double x1 = pointsInYear[nyr].get(ns, 1);
                double y2 = pointsInYear[nyr].get(ns + 1, 2);
                double y1 = pointsInYear[nyr].get(ns, 2);

                double m = (y2 - y1) / (x2 - x1);
                pointsInYear[nyr].set(ns + 1, 3, m);
                double b = y2 - m * x2;
                pointsInYear[nyr].set(ns + 1, 4, b);

                //System.out.println("nyr = " + nyr+ "\n ns= " + ns + "\n x1 = " + x1 + "\n y1=" +y1+ "\n m= " +m +"\n b= " +b);

                if (ns + 2 < scenarios.rows()) {
                    double x3 = pointsInYear[nyr].get(ns + 2, 1);
                    //System.out.println("\n x3= " + x3);
                    if (x3 == x2) {
                        //see if there are more with the same x value
                        //average the gdp values
                        int s = 2;
                        //System.out.println("\n x2 = x3 "+ s);
                        double gdpTotal = y2 + pointsInYear[nyr].get(ns + 2, 2);
                        while (ns + s + 1 < scenarios.rows()) {
                            if (pointsInYear[nyr].get(ns + s + 1, 1) != x2) {
                                break;
                            }
                            gdpTotal += pointsInYear[nyr].get(ns + s + 1, 2);
                            s++;
                        }


                        double gdpAve = gdpTotal / s;
                        double pceStep = (pointsInYear[nyr].get(ns + s, 1) - pointsInYear[nyr].get(ns, 1)) / s;
                        double gdpStep = gdpAve / s;
                        //calculate m and b for is scenarios

                        m = (gdpAve - y1) / (x2 - x1);
                        b = gdpAve - m * x2;
                        //System.out.println("\n pceStep= " +pceStep+ "\n gdpStep= " +gdpStep);

                        for (int i = 0; i < s; i++) {
                            //set intermediate GDP values and CE values

                            pointsInYear[nyr].set(ns + 1 + i, 1, x1 + pceStep * (i + 1));
                            pointsInYear[nyr].set(ns + 1 + i, 2, y1 + gdpStep * (i + 1));
                            pointsInYear[nyr].set(ns + 1 + i, 3, m);
                            pointsInYear[nyr].set(ns + 1 + i, 4, b);
                            //System.out.println("\n x1= " + x1 + "\n i= "+ i);
                        }
                    }
                }

            }

        }
        //put results in response surface
        for (int ns = scenarios.rows() - 1; ns >= 1; ns--) {

            Slice<Float, Integer> pces = new Slice<Float, Integer>();

            for (int nyr = scenarios.columns() - 1; nyr > baselineCol; nyr--) {
                Polynomial mxpb = new Polynomial(new double[]{pointsInYear[nyr].get(ns, 4), pointsInYear[nyr].get(ns, 3)});
                SliceSegment<Float, Integer> pcess = new SliceSegment<Float, Integer>(new Float(pointsInYear[nyr].get(ns, 1)), new Float(pointsInYear[nyr].get(ns - 1, 1)), nyr, mxpb);

                pces.add(pcess);
            }

            rs.addSlice(pces);

        }


        return rs;
    }
}
