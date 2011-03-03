package edu.mit.cci.simulation.model;

/**
 * User: jintrone
 * Date: 2/21/11
 * Time: 9:08 PM
 */
public class SimulationException extends Exception {

    public SimulationException(String message) {
        super(message);
    }

    public SimulationException(Exception e) {
        super(e);
    }


    public SimulationException(String message,Throwable t) {
        super(message, t);
    }

}
