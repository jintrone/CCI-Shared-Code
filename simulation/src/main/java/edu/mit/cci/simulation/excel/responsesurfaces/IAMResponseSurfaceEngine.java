package edu.mit.cci.simulation.excel.responsesurfaces;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * User: jintrone
 * Date: 2/24/11
 * Time: 11:02 PM
 *
 * A concrete response surface engine for generating response surfaces from published scenarios
 * from a variety of integrated assessment models.
 *
 * It is presumed that adjacent datasets are defined for various levels of emissions or atmospheric carbon
 * at each year - this is the "scenarios" matrix. The response surface will be evaluated at each year (an Integer) between adjacent
 * datasets.  The "output" matrix captures GDP.
 *
 *
 */

public class IAMResponseSurfaceEngine implements ResponseSurfaceEngine<Float,Integer> {

    /**
     *
     * @param cols Years for which scenarios are evaluated.
     * @param baselineIdx The baseline year against which emissions input is compared to
     * @param scenarios Each row is a scenario of emissions values over year
     * @param output Each row is a the GDP output / year
     * @return the response surface
     */
    public SimpleResponseSurface<Float, Integer> generateResponseSurface(Integer baselineIdx, Integer[] cols, DoubleMatrix2D scenarios, DoubleMatrix2D output) {
        if (output.rows() != scenarios.rows() || output.columns()!=scenarios.columns() || scenarios.columns() !=cols.length) {
            throw new IllegalArgumentException("Rows and / or cols do not match matrix dimensions");
        }

        if (baselineIdx!=null) {
            boolean found = false;
            for (int col:cols) {
                if (col == baselineIdx) {
                    found = true;
                    break;
                }
            }
            if (!found) throw new IllegalArgumentException("Baseline year not found in columns");
        }



        //TODO complete me

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
