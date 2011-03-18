package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Simulation;
import edu.mit.cci.simulation.model.Variable;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * User: jintrone
 * Date: 3/18/11
 * Time: 1:26 AM
 */
public class VariableJAXBAdapter  extends XmlAdapter<DefaultVariable, Variable> {

    @Override
    public Variable unmarshal(DefaultVariable defaultSimulation) throws Exception {
        return defaultSimulation;
    }

    @Override
    public DefaultVariable marshal(Variable v) throws Exception {
        return (DefaultVariable) v;
    }


}
