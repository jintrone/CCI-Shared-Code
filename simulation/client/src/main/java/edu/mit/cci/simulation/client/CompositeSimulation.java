package edu.mit.cci.simulation.client;

import java.util.Set;

/**
 * Represents a simulation that is composed of other simulations
 * @author jintrone
 *
 */
public interface CompositeSimulation extends Simulation {

    public Set<Simulation> getChildren();

    /**
     * An xml file that describes the composite simulation
     * @param descriptor
     */
    public void setCompositeDescriptor(String descriptor);
    public String getCompositeDescriptor();

}