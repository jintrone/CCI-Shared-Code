// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.Variable;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.Set;

privileged aspect DefaultSimulation_Roo_JavaBean {
    
    public Date DefaultSimulation.getCreated() {
        return this.created;
    }
    
    public void DefaultSimulation.setCreated(Date created) {
        this.created = created;
    }
    
    public Long DefaultSimulation.getSimulationVersion() {
        return this.simulationVersion;
    }
    
    public void DefaultSimulation.setSimulationVersion(Long simulationVersion) {
        this.simulationVersion = simulationVersion;
    }
    
    public String DefaultSimulation.getDescription() {
        return this.description;
    }
    
    public void DefaultSimulation.setDescription(String description) {
        this.description = description;
    }
    
    public String DefaultSimulation.getName() {
        return this.name;
    }
    
    public void DefaultSimulation.setName(String name) {
        this.name = name;
    }
    
    public String DefaultSimulation.getUrl() {
        return this.url;
    }
    
    public void DefaultSimulation.setUrl(String url) {
        this.url = url;
    }
    
    public Set<Variable> DefaultSimulation.getInputs() {
        return this.inputs;
    }
    
    public Set<Variable> DefaultSimulation.getOutputs() {
        return this.outputs;
    }
    
}
