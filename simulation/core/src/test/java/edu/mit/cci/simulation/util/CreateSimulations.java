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
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 2:15 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/webmvc-test-config.xml"})
public class CreateSimulations {


    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String SIM_MAP = "SimulationMapping.txt";
    public static String VAR_MAP = "VariableMapping.txt";

    public static void dumpMapping(Map<String, String> mapping, String mappingfile) throws IOException {
        File f = new File(mappingfile);
        PrintWriter writer = new PrintWriter(new FileWriter(f, true));
        for (Map.Entry<String, String> ents : mapping.entrySet()) {
            writer.println(ents.getKey() + "," + ents.getValue());
        }
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


        MappedSimulation tyndall_m = new MappedSimulation();
        tyndall_m.setSimulationVersion(1l);

        tyndall_m.setReplication(101);
        tyndall_m.setManyToOne(ManyToOneMapping.LAST);
        tyndall_m.setExecutorSimulation(tyndall);

        MappedSimulation ipcc_m = new MappedSimulation();
        ipcc_m.setSimulationVersion(1l);
        ipcc_m.setReplication(101);
        ipcc_m.setManyToOne(ManyToOneMapping.LAST);
        ipcc_m.setExecutorSimulation(ipcc);


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


        csim.persist();


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
        esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName("Temperature_Change", true), "Stern Review", "A9:A9"));
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
            map.put(v.getId_() + "", get(line, "id"));
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

}
