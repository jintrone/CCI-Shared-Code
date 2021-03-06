// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import java.lang.String;

privileged aspect MappedSimulation_Roo_ToString {
    
    public String MappedSimulation.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IdAsString: ").append(getIdAsString()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Created: ").append(getCreated()).append(", ");
        sb.append("SimulationVersion: ").append(getSimulationVersion()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Url: ").append(getUrl()).append(", ");
        sb.append("Inputs: ").append(getInputs()).append(", ");
        sb.append("Outputs: ").append(getOutputs()).append(", ");
        sb.append("ExecutorSimulation: ").append(getExecutorSimulation()).append(", ");
        sb.append("VariableMap: ").append(getVariableMap() == null ? "null" : getVariableMap().size()).append(", ");
        sb.append("Replication: ").append(getReplication()).append(", ");
        sb.append("SamplingFrequency: ").append(getSamplingFrequency()).append(", ");
        sb.append("ManyToOne: ").append(getManyToOne()).append(", ");
        sb.append("IndexingVariable: ").append(getIndexingVariable());
        return sb.toString();
    }
    
}
