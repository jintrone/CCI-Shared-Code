// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.DefaultScenario;
import java.util.Set;

privileged aspect ScenarioList_Roo_JavaBean {
    
    public Set<DefaultScenario> ScenarioList.getScenarios() {
        return this.scenarios;
    }
    
    public void ScenarioList.setScenarios(Set<DefaultScenario> scenarios) {
        this.scenarios = scenarios;
    }
    
}