package edu.mit.cci.simulation.excel.responsesurfaces;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * User: jintrone
 * Date: 2/24/11
 * Time: 10:20 PM
 *
 * Interface for an engine that computes a response surface.  For the SimpleResponseSurface defined here
 * columns are used to define points within each dataset where the the surface is re-evaluated.
 *
 * See implementing classes for concrete examples.
 */

public interface ResponseSurfaceEngine<T extends Comparable<T>,U extends Comparable<U>> {


    /**
     * Creates a response surface given the provided input data.  Input data is provided in the form of two matrices,
     * presumed to be organized as rows which describe different scenarios.
     *
     * Users are responsible for guaranteeing that
     * <ul>
     * <li>if a baselineidx is provided, that value exists in the other parameters</li>
     * <li>cols.length == scenarios.columns() == outputs.columns()</li>
     * </ul>
     *
     * @param cols Column headings; functions are defined between adjacent columns in the response surface
     * @param baselineidx Optionl Column value which should serve as a baseline if response surface
     * @param outputs Values that characterize the range of the response surface. Presume the first row is a reference scenario.
     * @param scenarios Values that characterize the domain of the response surface. Presument the first row correspond to the reference scenario.
     * @return A response surface
     */
    public SimpleResponseSurface<T,U> generateResponseSurface(U baselineidx, U[] cols, DoubleMatrix2D scenarios, DoubleMatrix2D outputs);

}
