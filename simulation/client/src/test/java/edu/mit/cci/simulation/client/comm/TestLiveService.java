package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/21/11
 * Time: 1:13 AM
 */
public class TestLiveService {

    @Test
    public void testPangaea() throws IOException, MetaDataNotFoundException, ScenarioNotFoundException, ModelNotFoundException {

        ClientRepository repo = ClientRepository.instance("localhost",8080);
        Simulation sim = repo.getSimulation(1L);

        Assert.assertNotNull(sim);
        Map<String, Object> inputs = new HashMap<String, Object>();
        //developed
        inputs.put("Developed start year", "2012");  //start year
        inputs.put("Developed target year", "2050");  //target year
        inputs.put("Pct change in Developed FF emissions", "0");   //target

        //developing a
        inputs.put("Developing A start year", "2012");
        inputs.put("Developing A target year", "2050");
        inputs.put("Pct change in Developing A FF emissions", "0");

        //developing b
        inputs.put("Developing B start year", "2012");
        inputs.put("Developing B target year", "2050");
        inputs.put("Pct change in Developing B FF emissions", "0");


        inputs.put("Target Sequestration", "0.50");  //sequestration (afforestation)
        inputs.put("Global land use emissions change", "0.50");  //deforestation
        Scenario scenario = repo.runModelWithInputNames(sim, inputs, 1L, true);
        Assert.assertNotNull(scenario);

    }

}
