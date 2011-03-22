// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.ManyToOneMapping;
import java.lang.Integer;
import java.util.Map;

privileged aspect MappedSimulation_Roo_JavaBean {
    
    public DefaultSimulation MappedSimulation.getExecutorSimulation() {
        return this.executorSimulation;
    }
    
    public Map<DefaultVariable, DefaultVariable> MappedSimulation.getVariableMap() {
        return this.variableMap;
    }
    
    public Integer MappedSimulation.getReplication() {
        return this.replication;
    }
    
    public Integer MappedSimulation.getSamplingFrequency() {
        return this.samplingFrequency;
    }
    
    public ManyToOneMapping MappedSimulation.getManyToOne() {
        return this.manyToOne;
    }
    
    public Variable MappedSimulation.getIndexingVariable() {
        return this.indexingVariable;
    }
    
}
