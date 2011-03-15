// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.MappedSimulation;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

privileged aspect MappedSimulationDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MappedSimulationDataOnDemand: @Component;
    
    private Random MappedSimulationDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<MappedSimulation> MappedSimulationDataOnDemand.data;
    
    public MappedSimulation MappedSimulationDataOnDemand.getSpecificMappedSimulation(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        MappedSimulation obj = data.get(index);
        return MappedSimulation.findMappedSimulation(obj.getId());
    }
    
    public MappedSimulation MappedSimulationDataOnDemand.getRandomMappedSimulation() {
        init();
        MappedSimulation obj = data.get(rnd.nextInt(data.size()));
        return MappedSimulation.findMappedSimulation(obj.getId());
    }
    
    public boolean MappedSimulationDataOnDemand.modifyMappedSimulation(MappedSimulation obj) {
        return false;
    }
    
    public void MappedSimulationDataOnDemand.init() {
        data = edu.mit.cci.simulation.model.MappedSimulation.findMappedSimulationEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'MappedSimulation' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<edu.mit.cci.simulation.model.MappedSimulation>();
        for (int i = 0; i < 10; i++) {
            edu.mit.cci.simulation.model.MappedSimulation obj = getNewTransientMappedSimulation(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
