package edu.mit.cci.simulation.model;

import java.util.Date;

/**
 * User: jintrone
 * Date: 2/10/11
 * Time: 3:07 PM
 */
public interface Scenario {
    public Simulation getSimulation();
    public Tuple getVariableValue(Variable v);
    public Date getCreated();


}

