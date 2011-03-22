package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.Simulation;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 11:01 PM
 */
public class SimulationJAXBAdapter extends XmlAdapter<DefaultSimulation, Simulation> {

    @Override
    public Simulation unmarshal(DefaultSimulation defaultSimulation) throws Exception {
        return defaultSimulation;
    }

    @Override
    public DefaultSimulation marshal(Simulation v) throws Exception {
        return (DefaultSimulation) v;
    }


}
