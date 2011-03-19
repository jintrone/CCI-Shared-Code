package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.model.transitional.AdaptedSimulation;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
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

    @Before
    public void setup() throws Exception {
        System.setProperty(RepositoryManager.CACHE_PROPERTY, 3 + "");
        repo = ClientRepository.instance("localhost", 8080);
        URL dirURL = ClientRepository.class.getResource("/marshalledelements/simple/");

        String[] files = new File(dirURL.toURI()).list();

        Map<String, String> responses = new HashMap<String, String>();
        for (String fs : files) {
            responses.put(fs.replace(".xml", ""), IOUtils.toString(ClientRepository.class.getResourceAsStream("/marshalledelements/simple/" + fs)));
        }


        MockConnector connector = new MockConnector(responses);
        RepositoryManager manager = new RepositoryManager(connector);
        setField(ClientRepository.class, repo, "deserializingConnector", connector);
        setField(ClientRepository.class, repo, "manager", manager);





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
        Assert.assertEquals("test",md.getName());

    }


    @Test
    public void testScenarioCache_growth() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {
//        System.setProperty(RepositoryManager.CACHE_PROPERTY,3+"");
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        Assert.assertEquals("Cache size should be equal to property setting",3,repo.getManager().currentScenarioCacheSize);
//        repo.getManager().clearCache();
//
//
//        Scenario old = TestHelper.runCompositeOne(repo);
//
//        List<Scenario> scenarios = new ArrayList<Scenario>();
//        scenarios.add(TestHelper.runCompositeOne(repo));
//        scenarios.add(TestHelper.runCompositeOne(repo));
//        scenarios.add(TestHelper.runCompositeOne(repo));
//
//        Assert.assertEquals("Cache size should only grow to size of 3",3,repo.getManager().scenarioCache.size());
//        Assert.assertTrue("Cache should contain last three elements accessed",repo.getManager().scenarioCache.values().containsAll(scenarios));
//        Assert.assertFalse("Cache should not contain first element added",repo.getManager().scenarioCache.values().contains(old));
    }

    @Test
    public void testScenarioCache_accessOrder() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {
//        System.setProperty(RepositoryManager.CACHE_PROPERTY,3+"");
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        RepositoryManager manager = repo.getManager();
//        Assert.assertEquals("Cache size should be equal to property setting",3,manager.currentScenarioCacheSize);
//        manager.scenarioCache.clear();
//
//
//        Scenario a = TestHelper.runCompositeOne(repo);
//
//        List<Scenario> scenarios = new ArrayList<Scenario>();
//        Scenario b = TestHelper.runCompositeOne(repo);
//        Scenario c = TestHelper.runCompositeOne(repo);
//
//        repo.getScenario(a.getId());
//        Scenario d = TestHelper.runCompositeOne(repo);
//
//        // should contain a, b, d
//
//        Assert.assertTrue("Cache should contain element ",manager.scenarioCache.values().contains(a));
//        Assert.assertTrue("Cache should contain element ",manager.scenarioCache.values().contains(c));
//        Assert.assertTrue("Cache should contain element ",manager.scenarioCache.values().contains(d));
//
//        Assert.assertFalse("Cache should not contain element ",manager.scenarioCache.values().contains(b));

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
