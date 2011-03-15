package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.SimulationException;

/**
 * User: jintrone
 * Date: 3/8/11
 * Time: 9:46 PM
 */
public class SimulationValidationException extends SimulationException {

    public SimulationValidationException(String s) {
        super(s);
    }

    public SimulationValidationException(Exception e) {
        super(e);
    }

    public SimulationValidationException(String message, Throwable t) {
        super(message, t);
    }
}
