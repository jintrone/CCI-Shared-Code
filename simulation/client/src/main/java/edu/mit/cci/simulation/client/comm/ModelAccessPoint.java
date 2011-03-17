package edu.mit.cci.simulation.client.comm;

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
    RUN_MODEL_URL("/simulations/defaultsimulations/%s/run"),
    GET_SIMULATION("/simulations/defaultsimulations"),
    GET_SCENARIO("/simulations/defaultscenarios"),
    GET_VARIABLE("/simulations/variables"),
    EDIT_SCENARIO_URL("/simulations/rest/scenariostate");

    String url;

    ModelAccessPoint(String s) {
       this.url = s;
    }

    public String create(InetAddress base, int port, String... params) {
        StringBuffer buf = new StringBuffer("http://");
        buf.append(base.getHostName());
        buf.append(":");
        buf.append(port);
        buf.append("a").append("b");
        int start = StringUtils.countMatches(url, "%");

        if (start > 0) {
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

    public static ModelAccessPoint forClass(Class clz) {
      if (clz==ClientMetaData.class) {
          return GET_VARIABLE;
      } else if (clz==ClientSimulation.class) {
          return GET_SIMULATION;
      } else if (clz == ClientScenario.class) {
          return GET_SCENARIO;
      }
        return null;
    }




}
