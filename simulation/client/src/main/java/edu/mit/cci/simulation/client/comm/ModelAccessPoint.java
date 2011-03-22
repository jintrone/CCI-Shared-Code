package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.model.impl.ClientMetaData;
import edu.mit.cci.simulation.client.model.impl.ClientScenario;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;
import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;

/**
* User: jintrone
* Date: 3/16/11
* Time: 4:12 PM
*/
public enum ModelAccessPoint implements RestAccessPoint {
    RUN_MODEL_URL("/simulation/defaultsimulations/%s/run"),
    GET_SIMULATION("/simulation/defaultsimulations"),
    GET_SCENARIO("/simulation/defaultscenarios"),
    GET_VARIABLE("/simulation/variables"),
    EDIT_SCENARIO_URL("/simulation/rest/scenariostate");

    String url;

    ModelAccessPoint(String s) {
       this.url = s;
    }

    public String create(InetAddress base, int port, String... params) {
        StringBuffer buf = new StringBuffer("http://");
        buf.append(base.getHostName());
        buf.append(":");
        buf.append(port);
        int start = StringUtils.countMatches(url, "%");

        if (start > -1) {
            buf.append(String.format(url,params));

        } else {
            buf.append(url);
        }
        for (int i =start;i<params.length;i++) {
            buf.append("/");
            buf.append(params[i]);
        }
        return buf.toString();
    }

    public static ModelAccessPoint forClass(Class clazz) {
       if (Simulation.class.isAssignableFrom(clazz) || edu.mit.cci.simulation.model.Simulation.class.isAssignableFrom(clazz)) {
            return GET_SIMULATION;
        } else if (MetaData.class.isAssignableFrom(clazz) || edu.mit.cci.simulation.model.Variable.class.isAssignableFrom(clazz)) {
            return GET_VARIABLE;
        } else if (Scenario.class.isAssignableFrom(clazz) || edu.mit.cci.simulation.model.Scenario.class.isAssignableFrom(clazz)) {
           return GET_SCENARIO;
        }
        return null;
    }




}
