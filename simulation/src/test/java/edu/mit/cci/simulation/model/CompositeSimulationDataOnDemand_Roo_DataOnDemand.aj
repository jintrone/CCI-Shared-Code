// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.CompositeSimulation;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

privileged aspect CompositeSimulationDataOnDemand_Roo_DataOnDemand {
    
    declare @type: CompositeSimulationDataOnDemand: @Component;
    
    private Random CompositeSimulationDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<CompositeSimulation> CompositeSimulationDataOnDemand.data;
    
    public CompositeSimulation CompositeSimulationDataOnDemand.getNewTransientCompositeSimulation(int index) {
        edu.mit.cci.simulation.model.CompositeSimulation obj = new edu.mit.cci.simulation.model.CompositeSimulation();
        obj.setInputs(null);
        obj.setOutputs(null);
        obj.setRunStrategy(null);
        obj.setCreated(new java.util.GregorianCalendar(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE), java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime());
        obj.setSimulationVersion(new Integer(index).longValue());
        obj.setDescription(null);
        obj.setName(null);
        obj.setUrl(null);
        return obj;
    }
    
    public CompositeSimulation CompositeSimulationDataOnDemand.getSpecificCompositeSimulation(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        CompositeSimulation obj = data.get(index);
        return CompositeSimulation.findCompositeSimulation(obj.getId());
    }
    
    public CompositeSimulation CompositeSimulationDataOnDemand.getRandomCompositeSimulation() {
        init();
        CompositeSimulation obj = data.get(rnd.nextInt(data.size()));
        return CompositeSimulation.findCompositeSimulation(obj.getId());
    }
    
    public boolean CompositeSimulationDataOnDemand.modifyCompositeSimulation(CompositeSimulation obj) {
        return false;
    }
    
    public void CompositeSimulationDataOnDemand.init() {
        data = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulationEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'CompositeSimulation' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<edu.mit.cci.simulation.model.CompositeSimulation>();
        for (int i = 0; i < 10; i++) {
            edu.mit.cci.simulation.model.CompositeSimulation obj = getNewTransientCompositeSimulation(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
