package edu.mit.cci.simulation.excel.responsesurfaces;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * User: jintrone
 * Date: 2/24/11
 * Time: 10:20 PM
 *
 * Interface for an engine that computes a response surface.  For the SimpleResponseSurface defined here
 * columns are used to define the bounds defined by adjacent datasets. Rows define points within each dataset
 * where the the surface is re-evaluated.
 *
 * See implementing classes for concrete examples.
 */

public interface ResponseSurfaceEngine<T extends Comparable<T>,U extends Comparable<U>> {


    /**
     * Creates a response surface given the provided input data
     *
     * @param cols Column headings; functions are defined between adjacent columns in the response surface
     * @param rows Row headings; functions are defined at each of the rows
     * @param m Matrix of raw data. Implementers should guarantee that m has the correct number of rows and columns
     * @return A response surface
     */
    public SimpleResponseSurface<T,U> generateResponseSurface(T[] cols,U[] rows, DoubleMatrix2D m);

}
