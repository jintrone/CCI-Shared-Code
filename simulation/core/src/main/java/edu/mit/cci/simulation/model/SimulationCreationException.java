package edu.mit.cci.simulation.model;

/**
 * User: jintrone
 * Date: 3/3/11
 * Time: 6:39 PM
 */
public class SimulationCreationException extends SimulationException {


    public SimulationCreationException(String message) {
        super(message);
    }

    public SimulationCreationException(Exception e) {
        super(e);
    }

    public SimulationCreationException(String message, Throwable t) {
        super(message, t);
    }
}
