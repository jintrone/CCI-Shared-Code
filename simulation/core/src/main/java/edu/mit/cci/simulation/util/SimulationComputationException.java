package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.SimulationException;

/**
 * User: jintrone
 * Date: 3/9/11
 * Time: 7:39 AM
 */
public class SimulationComputationException extends SimulationException {
    public SimulationComputationException(String s) {
        super(s);
    }

    public SimulationComputationException(Exception e) {
        super(e);
    }

    public SimulationComputationException(String message, Throwable t) {
        super(message, t);
    }
}
