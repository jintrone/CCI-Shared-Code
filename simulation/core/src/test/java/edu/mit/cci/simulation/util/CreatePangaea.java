package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Simulation;
import edu.mit.cci.simulation.model.Variable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class CreatePangaea {


    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @Transactional
    @Rollback(false)
    public void createPangaea() throws IOException, ParseException {
        Simulation sim = readSimulation("./target/test-classes/pangaea");
        addVars(sim, "./target/test-classes/pangaea", true, 1);
        addVars(sim, "./target/test-classes/pangaea", false, 101);
    }


    public static Simulation readSimulation(String basename) throws IOException, ParseException {

        CSVReader reader = new CSVReader(basename + "_sim.csv");
        Map<String, String> line = reader.readLine(1);

        DefaultSimulation sim = new DefaultSimulation();
        sim.setName(line.get("name"));
        sim.setDescription(line.get("description"));
        sim.setCreated(format.parse(line.get("creation")));
        sim.setUrl(line.get("url"));
        sim.setSimulationVersion(1l);
        sim.persist();
        return sim;
    }

    public static int inferPrecision(String profile) {
        if (profile.contains("Integer")) {
            return 0;
        } else return 2;
    }

    public static String[] parseCategories(String cat) {
        if ("NULL".equals(cat)) return null;
        else {
            return cat.split(",");
        }
    }


    public static void addVars(Simulation sim, String basename, boolean inputs, int arity) throws IOException {
        CSVReader reader = new CSVReader(basename + (inputs ? "_inputs.csv" : "_outputs.csv"));
        Map<String, Variable> varmap = new HashMap<String, Variable>();
        List<Variable> varlist = new ArrayList<Variable>();

        for (Map<String, String> line : reader) {

            DefaultVariable v = new DefaultVariable(line.get("name"),
                    line.get("description"),
                    arity, inferPrecision(line.get("profile")),
                    Double.parseDouble(line.get("min")),
                    Double.parseDouble(line.get("max")));
            v.setUnits(line.get("units"));
            v.setLabels(line.get("labels"));
            v.setExternalName(line.get("internalname"));
            v.setOptions(parseCategories(line.get("categories")));
            v.persist();
            varlist.add(v);
            varmap.put(line.get("metadata"), v);
        }

        int i = 0;
        for (Map<String, String> line : reader) {
            Variable v = varlist.get(i);
            if (!"NULL".equals(line.get("indexingid"))) {
                Variable indexing = varmap.get(line.get("indexingid"));
                v.setIndexingVariable(indexing);
                ((DefaultVariable) v).persist();


            }
            if (inputs) sim.getInputs().add(v);
            else sim.getOutputs().add(v);
            i++;
        }
        ((DefaultSimulation) sim).persist();
    }

}
