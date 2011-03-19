package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.comm.ClientRepository;
import edu.mit.cci.simulation.client.comm.ModelNotFoundException;
import edu.mit.cci.simulation.client.comm.ScenarioNotFoundException;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jintrone
 * Date: Jul 1, 2010
 * Time: 3:43:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestHelper {


    public static Scenario runCompositeOne(ClientRepository repo) throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {
       Simulation sim = repo.getSimulation(823L);

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
        return scenario;
    }

    public static Scenario runCompositeTwo(ClientRepository repo) throws IOException, MetaDataNotFoundException, ScenarioNotFoundException, ModelNotFoundException {
        Simulation sim = repo.getSimulation(981L);

        Map<String, Object> inputs = new HashMap<String, Object>();
        //developed
        inputs.put("Developed_Nations_Change_Start_Year_input2", "2012");  //start year
        inputs.put("Developed_Nations_Change_Target_Year_input3", "2050");  //target year
         inputs.put("US_EU_Emissions_Change_input0","100"); //US & EU
         inputs.put("Other_Developed_Change_input1", "100");   //other developed change

        //developing a
        inputs.put("Developing_Nations_Start_Year_input6", "2012");
        inputs.put("Developing_Nations_Target_Year_input7", "2013");
        inputs.put("China_India_Emissions_Change_input4", "100"); //china, india
        inputs.put("Other_Developing_Change_input5","100"); //other developing change

        //developing b
        inputs.put("Other_Nations_Start_Year_input10","2012");
        inputs.put("Other_Nations_Target_Year_input11","2050");
        inputs.put("Bloc_A_Emissions_Change_input8", "100"); //bloc a
        inputs.put("Bloc_B_Emissions_Change_input9", "100"); //bloc b


        inputs.put("Target Sequestration", "0.50");  //sequestration (afforestation)
        inputs.put("Global land use emissions change", "0.50");  //deforestation

        inputs.put("Percent_Transfer_from_Developed_to_Developing_input0", "50"); //percent transfer from developed to developing
        Scenario scenario = repo.runModelWithInputNames(sim, inputs, 1L, true);
        return scenario;
    }

}
