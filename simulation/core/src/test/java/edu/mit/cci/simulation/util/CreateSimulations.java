package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.excel.server.ExcelVariable;
import edu.mit.cci.simulation.model.*;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 2:15 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/webmvc-test-config.xml"})
public class CreateSimulations {


    private static final String SIMULATION_TYPE_PLAN = "type=plan";


	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String SIM_MAP = "SimulationMapping.txt";
    public static String VAR_MAP = "VariableMapping.txt";
    
    public static Long CSIM_3REGION_ID = 862L;
    public static Long CSIM_7REGION_ID = 863L;
    public static Long CSIM_15REGION_ID = 864L;

    public static void dumpMapping(Map<String, String> mapping, String mappingfile) throws IOException {
        File f = new File(mappingfile);
        PrintWriter writer = new PrintWriter(new FileWriter(f, true));
        for (Map.Entry<String, String> ents : mapping.entrySet()) {
            writer.println(ents.getKey() + "," + ents.getValue());
        }
        writer.flush();
        writer.close();
    }
    
    private void addMapping(Long from, Long to, String mappingfile) throws IOException {
        File f = new File(mappingfile);
        PrintWriter writer = new PrintWriter(new FileWriter(f, true));
	    writer.println(from + "," + to);
	    writer.flush();
	    writer.close();
    }

    public Simulation createBaseSim(String path, int inputArity, int outputArity) throws IOException, ParseException {

        Simulation sim = readSimulation(path);
        addVars(sim, path, true, inputArity);
        addVars(sim, path, false, outputArity);
        return sim;
    }


    @Test
    @Transactional
    @Rollback(false)
    public void createAll() throws IOException, ParseException, SimulationCreationException {
        new File(SIM_MAP).delete();
        new File(VAR_MAP).delete();
        DefaultSimulation pangaea = createPangaea();
        DefaultSimulation damage = createDamage();
        DefaultSimulation mitigation = createEMF22Mitigation();
        DefaultSimulation tyndall = createTyndall();
        DefaultSimulation ipcc = createIPCC();
        DefaultSimulation disag7 = create7RegionDisagregation();
        DefaultSimulation disag15 = create15RegionDisagregation();


        MappedSimulation tyndall_m = new MappedSimulation();
        tyndall_m.setSimulationVersion(1l);

        tyndall_m.setReplication(101);
        tyndall_m.setManyToOne(ManyToOneMapping.LAST);
        tyndall_m.setExecutorSimulation(tyndall);
        tyndall_m.setName("tyndall_m");
        tyndall_m.setDescription("tyndall_m");
        tyndall_m.setCreated(new Date());
        
        tyndall_m.persist();

        MappedSimulation ipcc_m = new MappedSimulation();
        ipcc_m.setSimulationVersion(1l);
        ipcc_m.setReplication(101);
        ipcc_m.setManyToOne(ManyToOneMapping.LAST);
        ipcc_m.setExecutorSimulation(ipcc);
        ipcc_m.setName("ipcc_m");
        ipcc_m.setDescription("tyndall_m");
        ipcc_m.setCreated(new Date());
        ipcc_m.persist();

        Simulation[] all = new Simulation[]{pangaea, damage, mitigation, tyndall_m, ipcc_m};



        CompositeSimulation csim = new CompositeSimulation();

        csim.setName("3 region composite model");
        csim.setSimulationVersion(1l);
        csim.setCreated(new Date());

        Step s1 = new Step(1, pangaea);
        Step s2 = new Step(2, mitigation, damage, tyndall_m, ipcc_m);

        csim.getSteps().add(s1);
        csim.getSteps().add(s2);


        for (Variable v : pangaea.getInputs()) {
            csim.getInputs().add(v);
        }
        for (Simulation s : all) {
            for (Variable v : s.getOutputs()) {
                csim.getOutputs().add(v);
            }
        }


        CompositeStepMapping mapping1 = new CompositeStepMapping(csim, null, s1);
        for (Variable v : csim.getInputs()) {
            mapping1.addLink(v, v);
        }
        
        CompositeStepMapping mapping2 = new CompositeStepMapping(csim, s1, s2);
        Variable pYear = pangaea.findVariableWithExternalName("Year", false);
        Variable pTemp = pangaea.findVariableWithExternalName("GlobalTempChange", false);
        Variable pCumEmissions = pangaea.findVariableWithExternalName("CumulativeEmissionsRel2005", false);

        mapping2.addLink(pTemp, damage.findVariableWithExternalName("Temperature_input0", true));
        mapping2.addLink(pYear, mitigation.findVariableWithExternalName("Time_input0", true));
        mapping2.addLink(pCumEmissions, mitigation.findVariableWithExternalName("CO2e_input1", true));
        mapping2.addLink(pTemp, tyndall_m.findVariableWithExternalName("Temperature_Change", true));
        mapping2.addLink(pTemp, ipcc_m.findVariableWithExternalName("Temperature_Change", true));

        CompositeStepMapping mapping3 = new CompositeStepMapping(csim, s2, null);
        for (Simulation s : s2.getSimulations()) {

            for (Variable v : s.getOutputs()) {

                mapping3.addLink(v, v);
            }
        }

        CompositeStepMapping mapping4 = new CompositeStepMapping(csim, s1, null);
        for (Simulation s : s1.getSimulations()) {

            for (Variable v : s.getOutputs()) {

                mapping4.addLink(v, v);
            }
        }

        csim.setType(SIMULATION_TYPE_PLAN);
        csim.persist();
        
        // add mapping for 3 region composite simulation
        addMapping(csim.getId(), CSIM_3REGION_ID, SIM_MAP);
        
        /*
         * 7 and 15 region disaggregation simulations 
         */

        
        /* inputs that should be ignored from pangaea as model inputs */
        Set<String> inputsNotIncludedInDisagModels = new HashSet<String>();
        inputsNotIncludedInDisagModels.add("Developed start year");
        inputsNotIncludedInDisagModels.add("Developed target year");
        inputsNotIncludedInDisagModels.add("Developing B start year");
        inputsNotIncludedInDisagModels.add("Developing B target year");
        inputsNotIncludedInDisagModels.add("Developing A start year");
        inputsNotIncludedInDisagModels.add("Developing A target year");
        inputsNotIncludedInDisagModels.add("Pct change in Developed FF emissions");
        inputsNotIncludedInDisagModels.add("Pct change in Developing A FF emissions");
        inputsNotIncludedInDisagModels.add("Pct change in Developing B FF emissions");

        /* Mapping from results of step 1 to step 2 (disag 7/15 region -> pangaea) */
        Map<String, String> disagOutputsToPangaeaMap = new HashMap<String, String>();
        disagOutputsToPangaeaMap.put("Developed_countries_start_year_output1", "Developed start year");
        disagOutputsToPangaeaMap.put("Rapidly_developing_countries_start_year_output4", "Developing A start year");
        disagOutputsToPangaeaMap.put("Other_developing_countries_start_year_output7", "Developing B start year");
        disagOutputsToPangaeaMap.put("Developed_countries_target_year_output2", "Developed target year");
        disagOutputsToPangaeaMap.put("Rapidly_developing_countries_target_year_output5", "Developing A target year");
        disagOutputsToPangaeaMap.put("Other_developing_countries_target_year_output8", "Developing B target year");
        disagOutputsToPangaeaMap.put("Developed_countries_emissions_change_output0", "Pct change in Developed FF emissions");
        disagOutputsToPangaeaMap.put("Rapidly_developing_countries_emissions_change_output3", "Pct change in Developing A FF emissions");
        disagOutputsToPangaeaMap.put("Other_developing_countries_emissions_change_output6", "Pct change in Developing B FF emissions");
        
        
        /*
         * 7 Region model
         */
        
        CompositeSimulation csim_7reg = new CompositeSimulation();
        csim_7reg.setName("7 region composite model");
        csim_7reg.setSimulationVersion(1l);
        csim_7reg.setCreated(new Date());
        
        Step s1_7reg = new Step(1, disag7);
        Step s2_7reg = new Step(2, pangaea);
        Step s3_7reg = new Step(3, mitigation, damage, tyndall_m, ipcc_m);

        csim_7reg.getSteps().add(s1_7reg);
        csim_7reg.getSteps().add(s2_7reg);
        csim_7reg.getSteps().add(s3_7reg);
        
        /* get inputs from disagregation 7 region sim */
        for (Variable v: disag7.getInputs()) {
        	csim_7reg.getInputs().add(v);
        }
        
        /* add inputs not covered by disagregation outputs */
        for (Variable v: pangaea.getInputs()) {
        	if (! inputsNotIncludedInDisagModels.contains(v.getExternalName())) {
        		csim_7reg.getInputs().add(v);
        	}
        }
        
        /* Same outputs as for 3-region */
        for (Simulation s : all) {
            for (Variable v : s.getOutputs()) {
            	csim_7reg.getOutputs().add(v);
            }
        }
        
        /* Mapping from input variables to input variables of disag7 simulation */
        CompositeStepMapping mapping1_7reg = new CompositeStepMapping(csim_7reg, null, s1_7reg);
        for (Variable v : disag7.getInputs()) {
            mapping1_7reg.addLink(v, v);
        }
        
        /* Map pangaea related inputs to step 2 (pangaea) */ 
        CompositeStepMapping mapping2_7reg = new CompositeStepMapping(csim_7reg, null, s2_7reg);
        for (Variable v: pangaea.getInputs()) {
        	System.out.println(v.getName() + "\t" + v.getExternalName());
        	if (! inputsNotIncludedInDisagModels.contains(v.getExternalName())) {
        		mapping2_7reg.addLink(v, v);
        	}
        }
        
        /* Map step 1 outputs to step 2 inputs */
        CompositeStepMapping mapping3_7reg = new CompositeStepMapping(csim_7reg, s1_7reg, s2_7reg);
        for (Variable v: disag7.getOutputs()) {
        	mapping3_7reg.addLink(v, pangaea.findVariableWithExternalName(disagOutputsToPangaeaMap.get(v.getExternalName()), true));
        }
        
        /* Map pangaea outputs to step 3 inputs (simmilar to 3 region) */
        CompositeStepMapping mapping4_7reg = new CompositeStepMapping(csim_7reg, s2_7reg, s3_7reg);
        
        mapping4_7reg.addLink(pTemp, damage.findVariableWithExternalName("Temperature_input0", true));
        mapping4_7reg.addLink(pYear, mitigation.findVariableWithExternalName("Time_input0", true));
        mapping4_7reg.addLink(pCumEmissions, mitigation.findVariableWithExternalName("CO2e_input1", true));
        mapping4_7reg.addLink(pTemp, tyndall_m.findVariableWithExternalName("Temperature_Change", true));
        mapping4_7reg.addLink(pTemp, ipcc_m.findVariableWithExternalName("Temperature_Change", true));

        /* Map step 3 outputs to composite sim outputs */
        CompositeStepMapping mapping5_7reg = new CompositeStepMapping(csim_7reg, s3_7reg, null);
        for (Simulation s : s3_7reg.getSimulations()) {
            for (Variable v : s.getOutputs()) {
            	mapping5_7reg.addLink(v, v);
            }
        }

        /* Map pangaea outputs to composite sim outputs */
        CompositeStepMapping mapping6_7reg = new CompositeStepMapping(csim_7reg, s2_7reg, null);
        for (Simulation s : s2_7reg.getSimulations()) {
            for (Variable v : s.getOutputs()) {
            	mapping6_7reg.addLink(v, v);
            }
        }
        
        csim_7reg.setType(SIMULATION_TYPE_PLAN);
        csim_7reg.persist();
        
        // add mapping for 3 region composite simulation
        addMapping(csim_7reg.getId(), CSIM_7REGION_ID, SIM_MAP);
        
        

        /*
         * 15 Region model
         */
        
        CompositeSimulation csim_15reg = new CompositeSimulation();
        csim_15reg.setName("15 region composite model");
        csim_15reg.setSimulationVersion(1l);
        csim_15reg.setCreated(new Date());
        
        Step s1_15reg = new Step(1, disag15);
        Step s2_15reg = new Step(2, pangaea);
        Step s3_15reg = new Step(3, mitigation, damage, tyndall_m, ipcc_m);

        csim_15reg.getSteps().add(s1_15reg);
        csim_15reg.getSteps().add(s2_15reg);
        csim_15reg.getSteps().add(s3_15reg);
        
        /* get inputs from disagregation 15 region sim */
        for (Variable v: disag15.getInputs()) {
        	csim_15reg.getInputs().add(v);
        }
        
        /* add inputs not covered by disagregation outputs */
        for (Variable v: pangaea.getInputs()) {
        	if (! inputsNotIncludedInDisagModels.contains(v.getExternalName())) {
        		csim_15reg.getInputs().add(v);
        	}
        }
        
        /* Same outputs as for 3-region */
        for (Simulation s : all) {
            for (Variable v : s.getOutputs()) {
            	csim_15reg.getOutputs().add(v);
            }
        }
        
        /* Mapping from input variables to input variables of disag15 simulation */
        CompositeStepMapping mapping1_15reg = new CompositeStepMapping(csim_15reg, null, s1_15reg);
        for (Variable v : disag15.getInputs()) {
            mapping1_15reg.addLink(v, v);
        }
        
        /* Map pangaea related inputs to step 2 (pangaea) */ 
        CompositeStepMapping mapping2_15reg = new CompositeStepMapping(csim_15reg, null, s2_15reg);
        for (Variable v: pangaea.getInputs()) {
        	if (! inputsNotIncludedInDisagModels.contains(v.getExternalName())) {
        		mapping2_15reg.addLink(v, v);
        	}
        }
        
        /* Map step 1 outputs to step 2 inputs */
        CompositeStepMapping mapping3_15reg = new CompositeStepMapping(csim_15reg, s1_15reg, s2_15reg);
        for (Variable v: disag15.getOutputs()) {
        	mapping3_15reg.addLink(v, pangaea.findVariableWithExternalName(disagOutputsToPangaeaMap.get(v.getExternalName()), true));
        }
        
        /* Map pangaea outputs to step 3 inputs (simmilar to 3 region) */
        CompositeStepMapping mapping4_15reg = new CompositeStepMapping(csim_15reg, s2_15reg, s3_15reg);
        
        mapping4_15reg.addLink(pTemp, damage.findVariableWithExternalName("Temperature_input0", true));
        mapping4_15reg.addLink(pYear, mitigation.findVariableWithExternalName("Time_input0", true));
        mapping4_15reg.addLink(pCumEmissions, mitigation.findVariableWithExternalName("CO2e_input1", true));
        mapping4_15reg.addLink(pTemp, tyndall_m.findVariableWithExternalName("Temperature_Change", true));
        mapping4_15reg.addLink(pTemp, ipcc_m.findVariableWithExternalName("Temperature_Change", true));

        /* Map step 3 outputs to composite sim outputs */
        CompositeStepMapping mapping5_15reg = new CompositeStepMapping(csim_15reg, s3_15reg, null);
        for (Simulation s : s3_15reg.getSimulations()) {
            for (Variable v : s.getOutputs()) {
            	mapping5_15reg.addLink(v, v);
            }
        }

        /* Map pangaea outputs to composite sim outputs */
        CompositeStepMapping mapping6_15reg = new CompositeStepMapping(csim_15reg, s2_15reg, null);
        for (Simulation s : s2_15reg.getSimulations()) {
            for (Variable v : s.getOutputs()) {
            	mapping6_15reg.addLink(v, v);
            }
        }
        
        csim_15reg.setType(SIMULATION_TYPE_PLAN);
        csim_15reg.persist();
        
        // add mapping for 3 region composite simulation
        addMapping(csim_15reg.getId(), CSIM_15REGION_ID, SIM_MAP);
        
        
    }



	//    @Test
//    @Transactional
//    @Rollback(false)
    public DefaultSimulation createPangaea() throws IOException, ParseException {
        Simulation sim = createBaseSim("./target/test-classes/pangaea", 1, 101);
        sim.setUrl("http://cognosis.mit.edu:8887/pangaea-servlet/rest/");
        ((DefaultSimulation) sim).persist();
        return (DefaultSimulation) sim;
    }

    //    @Test
//    @Transactional
//    @Rollback(false)
    public DefaultSimulation createEMF22Mitigation() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/emf22_mitigation", 101, 11);
        ExcelSimulation esim = new ExcelSimulation();
        esim.setSimulation(sim);
        esim.setCreation(new Date());
        esim.setFile(IOUtils.toByteArray(new FileInputStream("./target/test-classes/emf22_mitigation.xls")));

        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Time_input0", true), "Sheet2", "A13:A113"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("CO2e_input1", true), "Sheet2", "B13:B113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Time_output0", false), "Sheet3", "B13:B23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_witch_emf22_output1", false), "Sheet3", "B13:B23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_minicam_emf22_output2", false), "Sheet3", "C13:C23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_merge_emf22_output3", false), "Sheet3", "D13:D23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_fund_emf23_output4", false), "Sheet3", "E13:E23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_gcm_emf24_output5", false), "Sheet3", "F13:F23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_merge_optimistic_emf25_output6", false), "Sheet3", "G13:G23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_merge_pessimistic_emf26_output7", false), "Sheet3", "H13:H23"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Change_in_GDP_vs_baseline_message_emf26_output8", false), "Sheet3", "I13:I23"));

        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();
        return sim;


    }

    //    @Test
//    @Transactional
//    @Rollback(false)
    public DefaultSimulation createDamage() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/combined_damage", 101, 101);
        ExcelSimulation esim = new ExcelSimulation(sim, new File("./target/test-classes/combined_damage.xls"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_input0", true), "Sheet1", "A13:A113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Time1_output0", false), "Sheet2", "A13:A113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("change_in_GDP_vs_baseline_page_output1", false), "Sheet2", "B13:B113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("change_in_GDP_vs_baseline_page_5__output2", false), "Sheet2", "C13:C113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("change_in_GDP_vs_baseline_page_95__output3", false), "Sheet2", "D13:D113"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("change_in_GDP_vs_baseline_dice_output4", false), "Sheet2", "E13:E113"));

        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();
        return sim;

    }

    //    @Test
//    @Transactional
//    @Rollback(false)
    public DefaultSimulation createTyndall() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/tyndall", 1, 1);
        ExcelSimulation esim = new ExcelSimulation(sim, new File("./target/test-classes/tyndall.xls"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_Change", true), "Stern Review", "A2:A2"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_Change_output", false), "Stern Review", "A26:A26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Water_Impacts_output", false), "Stern Review", "B26:B26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Food_Impacts_output", false), "Stern Review", "C26:C26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Health_Impacts_output", false), "Stern Review", "D26:D26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Land_Impacts_output", false), "Stern Review", "E26:E26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Environment_Impacts_output", false), "Stern Review", "F26:F26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Abrupt_and_Large_Scale_Impacts_output", false), "Stern Review", "G26:G26"));
        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();
        return sim;
    }

    //    @Test
//    @Transactional
//    @Rollback(false)
    public DefaultSimulation createIPCC() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/ipcc", 1, 1);
        ExcelSimulation esim = new ExcelSimulation(sim, new File("./target/test-classes/ipcc.xls"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_Change", true), "Stern Review", "A13:A13"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_Change1_output", false), "Stern Review", "A26:A26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Water1_output", false), "Stern Review", "B26:B26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Food_Agriculture1_output", false), "Stern Review", "C26:C26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Health1_output", false), "Stern Review", "D26:D26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Land_Coastal1_output", false), "Stern Review", "E26:E26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Environment_Ecosystems1_output", false), "Stern Review", "F26:F26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Abrupt_Singular_Events1_output", false), "Stern Review", "G26:G26"));
        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();
        return sim;
    }


    public static Simulation readSimulation(String basename) throws IOException, ParseException {

        Map<String, String> map = new HashMap<String, String>();
        CSVReader reader = new CSVReader(basename + "_sim.csv");
        Map<String, String> line = reader.readLine(1);

        DefaultSimulation sim = new DefaultSimulation();
        sim.setName(get(line, "name"));
        sim.setDescription(get(line, "description"));
        sim.setCreated(format.parse(get(line, "creation")));
        sim.setUrl(get(line, "url"));
        sim.setSimulationVersion(1l);
        sim.persist();
        map.put(sim.getId() + "", line.get("id"));
        dumpMapping(map, SIM_MAP);
        return sim;
    }

    public static int inferPrecision(String profile) {
        if (profile.contains("Integer")) {
            return 0;
        } else return 2;
    }

    public static DataType inferDataType(String vartype) {
        if ("RANGE".equals(vartype)) {
            return DataType.NUM;
        } else if ("CATEGORICAL".equals(vartype)) {
            return DataType.CAT;
        } else if ("FREE".equals(vartype)) {
            return DataType.TXT;
        }
        return DataType.NUM;
    }


    public static String[] parseCategories(String cat) {
        if (cat == null) return null;
        else {
            return cat.split(",");
        }
    }

    public static String get(Map<String, String> line, String key) {
        String result = line.get(key);
        return "NULL".equals(result) || result.isEmpty() ? null : result;
    }

    public static Long getLong(Map<String, String> line, String key) {
        String result = get(line, key);
        return result == null ? null : Long.parseLong(result);
    }

    public static Double getDouble(Map<String, String> line, String key) {
        String result = get(line, key);
        return result == null ? null : Double.parseDouble(result);
    }


    public static void addVars(Simulation sim, String basename, boolean inputs, int arity) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        CSVReader reader = new CSVReader(basename + (inputs ? "_inputs.csv" : "_outputs.csv"));
        Map<String, Variable> varmap = new HashMap<String, Variable>();
        List<Variable> varlist = new ArrayList<Variable>();

        for (Map<String, String> line : reader) {

            DefaultVariable v = new DefaultVariable(get(line, "name"),
                    get(line, "description"),
                    arity, inferPrecision(get(line, "profile")),
                    getDouble(line, "min"),
                    getDouble(line, "max"));
            v.setUnits(get(line, "units"));
            v.setLabels(get(line, "labels"));
            v.setExternalName(get(line, "internalname"));
            v.setOptions(parseCategories(get(line, "categories")));
            v.setDataType(inferDataType(get(line, "vartype")));

            v.persist();
            map.put(v.getId_() + "", get(line, "metadata"));
            varlist.add(v);
            varmap.put(get(line, "metadata"), v);
        }

        int i = 0;
        for (Map<String, String> line : reader) {
            Variable v = varlist.get(i);
            if (get(line, "indexingid") != null) {
                Variable indexing = varmap.get(get(line, "indexingid"));
                v.setIndexingVariable(indexing);
                ((DefaultVariable) v).persist();


            }
            if (inputs) sim.getInputs().add(v);
            else sim.getOutputs().add(v);
            i++;
        }
        ((DefaultSimulation) sim).persist();
        dumpMapping(map, VAR_MAP);
    }
    

	private DefaultSimulation create15RegionDisagregation() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/disag15", 1, 1);
        ExcelSimulation esim = new ExcelSimulation(sim, new File("./target/test-classes/disag15.xls"));
        
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("US_emissions_change_input0", true), "15region", "A13:A13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("EU_emissions_change_input1", true), "15region", "B13:B13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Russia_Former_Soviet_Union_emissions_change_input2", true), "15region", "C13:C13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("OECD_Asia_emissions_change_input3", true), "15region", "D13:D13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Canada_emissions_change_input4", true), "15region", "E13:E13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_start_year_input5", true), "15region", "F13:F13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_target_year_input6", true), "15region", "G13:G13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("China_emissions_change_input7", true), "15region", "H13:H13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("India_emissions_change_input8", true), "15region", "I13:I13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Brazil_emissions_change_input9", true), "15region", "J13:J13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("South_Africa_emissions_change_input10", true), "15region", "K13:K13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Mexico_emissions_change_input11", true), "15region", "L13:L13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_Asia_emissions_change_input12", true), "15region", "M13:M13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_start_year_input13", true), "15region", "N13:N13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_target_year_input14", true), "15region", "O13:O13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Middle_East_input15", true), "15region", "P13:P13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Latin_America_input16", true), "15region", "Q13:Q13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Africa_input17", true), "15region", "R13:R13"));
		esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_Asia_input18", true), "15region", "S13:S13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_start_year_input19", true), "15region", "T13:T13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_target_year_input20", true), "15region", "U13:U13"));

        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_emissions_change_output0", false), "15region", "A26:A26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_start_year_output1", false), "15region", "B26:B26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_target_year_output2", false), "15region", "C26:C26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_emissions_change_output3", false), "15region", "D26:D26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_start_year_output4", false), "15region", "E26:E26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_target_year_output5", false), "15region", "F26:F26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_emissions_change_output6", false), "15region", "G26:G26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_start_year_output7", false), "15region", "H26:H26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_target_year_output8", false), "15region", "I26:I26"));
        
        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();        
        
        return sim;
    }

	private DefaultSimulation create7RegionDisagregation() throws IOException, ParseException {
        DefaultSimulation sim = (DefaultSimulation) createBaseSim("./target/test-classes/disag7", 1, 1);
        ExcelSimulation esim = new ExcelSimulation(sim, new File("./target/test-classes/disag7.xls"));
        
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("US_emissions_change_input0", true), "Disagge", "A13:A13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("EU_emissions_change_input1", true), "Disagge", "B13:B13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developed_countries_emissions_change_input2", true), "Disagge", "C13:C13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_start_year_input3", true), "Disagge", "D13:D13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_target_year_input4", true), "Disagge", "E13:E13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("China_emissions_change_input5", true), "Disagge", "F13:F13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("India_emissions_change_input6", true), "Disagge", "G13:G13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_rapidly_developing_countries_emissions_change_input7", true), "Disagge", "H13:H13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_start_year_input8", true), "Disagge", "I13:I13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_target_year_input9", true), "Disagge", "J13:J13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_emissions_change_input10", true), "Disagge", "K13:K13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_start_year_input11", true), "Disagge", "L13:L13"));
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_target_year_input12", true), "Disagge", "M13:M13"));
        

        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_emissions_change_output0", false), "Disagge", "A26:A26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_start_year_output1", false), "Disagge", "B26:B26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Developed_countries_target_year_output2", false), "Disagge", "C26:C26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_emissions_change_output3", false), "Disagge", "D26:D26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_start_year_output4", false), "Disagge", "E26:E26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Rapidly_developing_countries_target_year_output5", false), "Disagge", "F26:F26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_emissions_change_output6", false), "Disagge", "G26:G26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_start_year_output7", false), "Disagge", "H26:H26"));
        esim.getOutputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Other_developing_countries_target_year_output8", false), "Disagge", "I26:I26"));
        
        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL + esim.getId());
        sim.persist();
        return sim;
    }


}
