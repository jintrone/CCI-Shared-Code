package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.MockHttpServer;
import edu.mit.cci.simulation.util.U;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class DefaultSimulationTest {

    @Autowired
    private DefaultSimulationDataOnDemand dod;

    @Autowired
    private VariableDataOnDemand vdod;

    @Autowired
    private TupleDataOnDemand tdod;


    @Test
    public void testRunSimulation() throws IOException, SimulationException {


        DefaultSimulation sim = dod.getRandomDefaultSimulation();

        sim.setUrl("http://localhost:8080/canned");

        Variable one = new Variable("Test1","Test",3);
        Variable two = new Variable("Test2","Test",3);
        Variable three = new Variable("Test3","Test",3);
        Variable four = new Variable("Test4","Test",3);
        one.persist();
        two.persist();
        three.persist();
        four.persist();

        Tuple onet = new Tuple(one);
        Tuple twot = new Tuple(two);



        String[] onev = new String[] {"1","2","3"};
        String[] twov = new String[] {"4","5","6"};
        String[] threev = new String[] {"7","8","9"};
        String[] fourv = new String[] {"10","11","12"};

        onet.setValues(onev);
        twot.setValues(twov);

        onet.setVar(one);
        twot.setVar(two);

        List<Tuple> inputs = new ArrayList<Tuple>();
        inputs.add(onet);
        inputs.add(twot);

        sim.setInputs(new HashSet<Variable>());
        sim.setOutputs(new HashSet<Variable>());
        sim.getInputs().add(one);
        sim.getInputs().add(two);
        sim.getOutputs().add(three);
        sim.getOutputs().add(four);

        Map<Variable,String[]> m = new HashMap<Variable,String[]>();
        m.put(three,threev);
        m.put(four,fourv);
        String expect = U.createStringRepresentation(m);
        MockHttpServer server = new MockHttpServer();
        server.setCannedResponse(expect);
        Closeable c = server.run("http://localhost:8080/");

        Scenario result = sim.run(inputs);
        c.close();

        Assert.assertArrayEquals(result.getVariableValue(one).getValues(),onev);
         Assert.assertArrayEquals(result.getVariableValue(two).getValues(),twov);
         Assert.assertArrayEquals(result.getVariableValue(three).getValues(),threev);
         Assert.assertArrayEquals(result.getVariableValue(four).getValues(),fourv);
    }



}
