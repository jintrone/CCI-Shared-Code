package edu.mit.cci.simulation.client;

import edu.mit.cci.simulation.client.comm.RepositoryManagerFactory;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;

import edu.mit.cci.simulation.jaxb.JaxbUtils;
import edu.mit.cci.simulation.model.*;
import edu.mit.cci.simulation.util.ConcreteSerializableCollection;
import edu.mit.cci.simulation.jaxb.JaxbCollection;
import edu.mit.cci.simulation.jaxb.JaxbReference;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.locks.Condition;

/**
 * @author: jintrone
 * @date: May 17, 2010
 */
public class UnmarshallingTest {


    private static Logger log = Logger.getLogger(UnmarshallingTest.class);

    @Test
    public void testUnmarshall() throws FileNotFoundException, JAXBException {


        System.setProperty(JaxbUtils.RESOLVER_FACTORY_PROPERTY,"edu.mit.cci.simulation.client.comm.TestResolver");

        JAXBContext context = JAXBContext.newInstance(DefaultSimulation.class, DefaultVariable.class, DefaultScenario.class,
                ConcreteSerializableCollection.class, JaxbCollection.class, JaxbReference.class);
        System.setProperty("jaxb.debug", "true");


        Unmarshaller um = context.createUnmarshaller();
        InputStream stream = ClientSimulation.class.getResourceAsStream("/marshalledelements/simple/DefaultSimulations.xml");
        Object o = um.unmarshal(stream);

        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof ConcreteSerializableCollection);
        edu.mit.cci.simulation.model.Simulation sim = (edu.mit.cci.simulation.model.Simulation) ((ConcreteSerializableCollection)o).bucket.toArray()[0];
        Assert.assertTrue(sim.getInputs().size() ==1);
        Assert.assertTrue("fooey".equals(sim.getInputs().toArray()[0]));

    }

//    @Test
//    public void testRepositoryRetrieval_Simulations() throws IOException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        Collection<Simulation> sims = repo.getAllSimulations();
//        log.info("Retrieved " + sims.size() + " simulations:");
//        for (Simulation sim : sims) {
//            log.info(sim.getName());
//            StringBuffer buf = new StringBuffer();
//            buf.append("-- Inputs\n");
//            for (MetaData md : sim.getInputs()) {
//                buf.append("---- ").append(md.getId()).append(":").append(md.getName()).append(" - ").append(md.getDescription());
//                buf.append("\n");
//            }
//            log.info(buf.toString());
//            buf = new StringBuffer();
//            buf.append("-- Outputs\n");
//            for (MetaData md : sim.getOutputs()) {
//                buf.append("---- ").append(md.getId()).append(":").append(md.getName()).append(" - ").append(md.getDescription());
//                buf.append(" : isIndex - ").append(md.getIndex());
//                buf.append(" : indexingmd - "+(md.getIndexingMetaData()!=null?md.getIndexingMetaData().getId():"<none>"));
//
//                buf.append("\n");
//            }
//            buf.append("\n");
//            log.info(buf);
//        }
//
//    }
//
//    @Test
//    public void testRepositoryRetrieval_Scenario() throws IOException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//
//        Scenario s = repo.getScenario(2703l);
//        Assert.assertNotNull(s);
//        log.info("Scenario: " + s.getName());
//        log.info(getScenarioString(s));
//
//    }
//
//
//    @Test
//    public void testCompositeModelRun() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        Scenario scenario = TestHelper.runCompositeOne(repo);
//        log.info("Scenario: "+scenario.getName()+" id:"+scenario.getId());
//        log.info(getScenarioString(scenario));
//        Assert.assertEquals(EntityState.TEMPORARY,scenario.getState());
//
//    }
//       @Test
//     public void testCompositeModelRun2() throws IOException, ScenarioNotFoundException, ModelNotFoundException, MetaDataNotFoundException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        Scenario scenario = TestHelper.runCompositeTwo(repo);
//        log.info("Scenario: "+scenario.getName()+" id:"+scenario.getId());
//        log.info(getScenarioString(scenario));
//        Assert.assertEquals(EntityState.TEMPORARY,scenario.getState());
//
//    }
//
//    @Test
//    public void testTypeField() throws IOException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//         Collection<Simulation> sims = repo.getAllSimulations();
//        log.info("Retrieved " + sims.size() + " simulations:");
//        for (Simulation s:repo.getAllSimulations()) {
//            log.info("Retrieved: "+s.getName()+" - "+s.getId());
//        }
//        Simulation sim = repo.getSimulation(920L);
//        Assert.assertNotNull(sim);
//        Assert.assertEquals("test test",sim.getType());
//    }

   

//    @Test
//    public void testModelUpdate() throws IOException, ScenarioNotFoundException, ModelNotFoundException {
//        ClientRepository repo = ClientRepository.instance("localhost", 8080);
//        Simulation s = repo.getSimulation(621L);
//        String name = s.getName();
//        s.setName("fooey");
//        repo.updateSimulation(s);
//
//        s = repo.getSimulation(621L);
//        Assert.assertEquals("fooey",s.getName());
//
//        s.setName(name);
//        repo.updateSimulation(s);
//
//        s = repo.getSimulation(621L);
//        Assert.assertEquals(name,s.getName());
//
//    }


//    private static String getScenarioString(Scenario s) {
//        List<DefaultVariable> inputs = s.getInputSet();
//        StringBuffer buf = new StringBuffer();
//        buf.append("Inputs\n");
//        for (DefaultVariable v : inputs) {
//            buf.append(v.getMetaData().getName()).append(":").append(v.getMetaData().getId()).append(":");
//            buf.append(v.getValue().toString());
//            buf.append("\n");
//        }
//        buf.append("\n");
//        List<DefaultVariable> outputs = s.getOutputSet();
//        buf.append("Outputs\n");
//        for (DefaultVariable v : outputs) {
//            buf.append(v.getMetaData().getName()).append(":").append(v.getMetaData().getId()).append(":");
//            buf.append(v.getValue().toString()).append(":");
//            buf.append("\n");
//        }
//        return buf.toString();
//    }

   


}
