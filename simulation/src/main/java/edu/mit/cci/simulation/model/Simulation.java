package edu.mit.cci.simulation.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface Simulation {

    public String getName();
    public String getDescription();
    public Long getSimulationVersion();
    public Date getCreated();


    public Set<Variable> getInputs();
    public Set<Variable> getOutputs();

    public String getUrl();


    Scenario run(List<Tuple> siminputs) throws SimulationException;
}
