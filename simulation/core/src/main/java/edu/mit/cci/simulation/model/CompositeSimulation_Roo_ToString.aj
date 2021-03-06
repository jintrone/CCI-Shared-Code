// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import java.lang.String;

privileged aspect CompositeSimulation_Roo_ToString {
    
    public String CompositeSimulation.toString() {
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
        sb.append("Steps: ").append(getSteps() == null ? "null" : getSteps().size()).append(", ");
        sb.append("StepMapping: ").append(getStepMapping() == null ? "null" : getStepMapping().size());
        return sb.toString();
    }
    
}
