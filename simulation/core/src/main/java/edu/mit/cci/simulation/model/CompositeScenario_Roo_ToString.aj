// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import java.lang.String;

privileged aspect CompositeScenario_Roo_ToString {
    
    public String CompositeScenario.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IdAsString: ").append(getIdAsString()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Simulation: ").append(getSimulation()).append(", ");
        sb.append("Values_: ").append(getValues_() == null ? "null" : getValues_().size()).append(", ");
        sb.append("Created: ").append(getCreated()).append(", ");
        sb.append("ChildScenarios: ").append(getChildScenarios() == null ? "null" : getChildScenarios().size()).append(", ");
        sb.append("LastStep: ").append(getLastStep());
        return sb.toString();
    }
    
}
