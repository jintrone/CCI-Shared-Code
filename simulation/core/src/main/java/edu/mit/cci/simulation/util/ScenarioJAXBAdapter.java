package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultScenario;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.Scenario;
import edu.mit.cci.simulation.model.Simulation;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 11:01 PM
 */
public class ScenarioJAXBAdapter  extends XmlAdapter<DefaultScenario, Scenario> {

    @Override
    public Scenario unmarshal(DefaultScenario defaultScenario) throws Exception {
        return defaultScenario;
    }

    @Override
    public DefaultScenario marshal(Scenario v) throws Exception {
        return (DefaultScenario) v;
    }
}
