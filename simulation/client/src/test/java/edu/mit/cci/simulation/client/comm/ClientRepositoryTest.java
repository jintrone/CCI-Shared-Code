package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.Variable;
import edu.mit.cci.simulation.client.model.transitional.AdaptedSimulation;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.plaf.metal.MetalIconFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jintrone
 * Date: Jul 1, 2010
 * Time: 3:41:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRepositoryTest {


    ClientRepository repo;
    MockConnector connector;

    @Before
    public void setup() throws Exception {
        System.setProperty(RepositoryManager.CACHE_PROPERTY, 3 + "");

        URL dirURL = ClientRepository.class.getResource("/marshalledelements/");
        URI basedir = dirURL.toURI();
        String[] files = new File(basedir).list();

        Map<String, String> responses = new HashMap<String, String>();
        for (String fs : files) {
            File test = new File(basedir.getPath()+"/"+fs);
            if (test.isDirectory()) continue;
            responses.put(fs.replace(".xml", ""), IOUtils.toString(ClientRepository.class.getResourceAsStream("/marshalledelements/" + fs)));
        }


        connector = new MockConnector(responses);
        RepositoryManager manager = new RepositoryManager(connector);
         repo = ClientRepository.instance(manager,connector);





    }

    @Test
    public void testScenarioCache_Size() throws IOException {
        Assert.assertEquals("Cache size should be equal to property setting", 3, repo.getManager().currentScenarioCacheSize);

    }

    @Test
    public void testInit() throws IOException {
        repo.getManager().clearCache();
        Assert.assertEquals(0, repo.getAllSimulations().size());
        repo.getManager().refreshSimulations();
        Assert.assertEquals(1,repo.getAllSimulations().size());
        Assert.assertTrue(((AdaptedSimulation)repo.getAllSimulations().toArray()[0]).getInputs().size()>0);
        MetaData md = ((Simulation)repo.getAllSimulations().toArray()[0]).getInputs().get(0);
        Assert.assertNotNull(md);

    }

    @Test
    public void testScenario() throws IOException {
        Scenario s = repo.getScenario(3l);
        Assert.assertNotNull(s);
        Assert.assertEquals(8, s.getOutputSet().size());

        for (Variable v:s.getOutputSet()) {
            Assert.assertEquals(101,v.getValue().size());
            if (v.getMetaData().getIndex()) {
                Assert.assertArrayEquals(new String[]{"2000"},v.getValue().get(0).getValues());

            } else {
                Assert.assertEquals(2,v.getValue().get(0).getValues().length);
                Assert.assertEquals("2000",v.getValue().get(0).getValues()[0]);
            }
        }


    }


    @Test
    public void testScenarioCache_growth() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {

        Assert.assertEquals("Cache size should be equal to property setting",3,repo.getManager().currentScenarioCacheSize);
        String s = connector.responses.get("DefaultScenarios.3");
        connector.responses.put("DefaultScenarios.4",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"4\">"));
        connector.responses.put("DefaultScenarios.5",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"5\">"));
        connector.responses.put("DefaultScenarios.6",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"6\">"));
        connector.responses.put("DefaultScenarios.7",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"7\">"));

        List<Scenario> scenarios = new ArrayList<Scenario>();
        Scenario old = repo.getScenario(3l);
        scenarios.add(repo.getScenario(4l));
        scenarios.add(repo.getScenario(5l));
        scenarios.add(repo.getScenario(6l));

        Assert.assertEquals("Cache size should only grow to size of 3",3,repo.getManager().scenarioCache.size());
        Assert.assertTrue("Cache should contain last three elements accessed",repo.getManager().scenarioCache.values().containsAll(scenarios));
        Assert.assertFalse("Cache should not contain first element added",repo.getManager().scenarioCache.values().contains(old));
    }

    @Test
    public void testScenarioCache_accessOrder() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {



        String s = connector.responses.get("DefaultScenarios.3");
        connector.responses.put("DefaultScenarios.4",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"4\">"));
        connector.responses.put("DefaultScenarios.5",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"5\">"));
        connector.responses.put("DefaultScenarios.6",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"6\">"));
        connector.responses.put("DefaultScenarios.7",s.replaceFirst("<Scenario Id=\"3\">","<Scenario Id=\"7\">"));


        Scenario a = repo.getScenario(3l);

        List<Scenario> scenarios = new ArrayList<Scenario>();
        Scenario b = repo.getScenario(4l);
        Scenario c = repo.getScenario(5l);
        repo.getScenario(a.getId());
        Scenario d = repo.getScenario(6l);

        // should contain a, c, d

        Assert.assertTrue("Cache should contain element ",repo.getManager().scenarioCache.values().contains(a));
        Assert.assertTrue("Cache should contain element ",repo.getManager().scenarioCache.values().contains(c));
        Assert.assertTrue("Cache should contain element ",repo.getManager().scenarioCache.values().contains(d));

        Assert.assertFalse("Cache should not contain element ",repo.getManager().scenarioCache.values().contains(b));

    }

    /**
     * <p>
     * Get value of given <code>Field</code> of given <code>Object</code>.
     * </p>
     *
     * @param clazz      Class to get declared field
     * @param object     instance to get field from
     * @param fieldName  name of field
     * @param fieldValue field value
     * @throws Exception to JUnit
     */
    @SuppressWarnings("all")
    public static void setField(Class clazz, Object object, String fieldName, Object fieldValue) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(object, fieldValue);
    }


}
