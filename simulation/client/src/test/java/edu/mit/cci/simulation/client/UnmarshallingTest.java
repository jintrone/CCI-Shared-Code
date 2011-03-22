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


        System.setProperty(JaxbUtils.RESOLVER_FACTORY_PROPERTY,"edu.mit.cci.simulation.client.comm.MockResolver");

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



   


}
