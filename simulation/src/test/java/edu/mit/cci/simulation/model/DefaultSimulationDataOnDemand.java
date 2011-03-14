package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = DefaultSimulation.class)
public class DefaultSimulationDataOnDemand {
      public DefaultSimulation getNewTransientDefaultSimulation(int index) {
        edu.mit.cci.simulation.model.DefaultSimulation obj = new edu.mit.cci.simulation.model.DefaultSimulation();
        obj.setInputs(null);
        obj.setOutputs(null);
        obj.setCreated(null);
        obj.setSimulationVersion(new Integer(index).longValue());
        obj.setDescription(null);
        obj.setName(null);
        obj.setUrl(null);
        return obj;
    }

}
