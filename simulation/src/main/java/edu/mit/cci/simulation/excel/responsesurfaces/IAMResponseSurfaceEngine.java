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
 * It is presumed that adjacent datasets are defined for various levels of emissions or atmospheric carbon;
 * this is the first parameter (a Float).  The response surface will be evaluated at each year (an Integer) between adjacent
 * datasets.  The dataset itself might contain some indication of GDP at each paricular point (e.g. deviation from baseline).
 *
 *
 */

public class IAMResponseSurfaceEngine implements ResponseSurfaceEngine<Float,Integer> {

    /**
     *
     * @param cols Emissions / Atm. CO2 levels in each year
     * @param rows The year
     * @param m Matrix of GDP indicators
     * @return
     */
    public SimpleResponseSurface<Float, Integer> generateResponseSurface(Float[] cols, Integer[] rows, DoubleMatrix2D m) {
        if (m.rows()!=rows.length || m.columns()!=cols.length) {
            throw new IllegalArgumentException("Rows and / or cols do not match matrix dimensions");
        }

        //TODO complete me

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
