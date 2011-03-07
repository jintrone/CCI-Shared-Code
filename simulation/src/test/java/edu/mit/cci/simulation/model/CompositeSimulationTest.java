package edu.mit.cci.simulation.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * User: jintrone
 * Date: 3/3/11
 * Time: 1:15 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CompositeSimulationTest {


    private SimulationMockFactory factory = new SimulationMockFactory();

    @Test
    public void testCreate_1() {

        CompositeSimulation csim = new CompositeSimulation();

        DefaultSimulation sim1 = new DefaultSimulation();


        DefaultSimulation sim2 = new DefaultSimulation();


        Step s1 = new Step(1,sim1);
        Step s2 = new Step(2,sim2);


        csim.getSteps().add(s1);
        csim.getSteps().add(s2);

        Assert.assertEquals(2,csim.getSteps().size());
        Assert.assertTrue(csim.getSteps().contains(s1));
        Assert.assertTrue(csim.getSteps().contains(s2));
    }

    @Test
    public void testCreate_2() throws SimulationCreationException {

        CompositeSimulation csim = new CompositeSimulation();




        DefaultSimulation sim1 = new DefaultSimulation();
        Variable v_in = factory.getVariable(1,"TestInput1",DataType.NUM,1);
        Variable v_out = factory.getVariable(1,"TestOutput1",DataType.NUM,1);
        sim1.getInputs().add(v_in);
        sim1.getOutputs().add(v_out);
        Step s1 = new Step(1,sim1);



        DefaultSimulation sim2 = new DefaultSimulation();
        Variable v_in_1 = factory.getVariable(2,"TestInput2",DataType.NUM,1);
        Variable v_in_2 = factory.getVariable(1,"TestInput3",DataType.TXT,1);
        Variable v_in_3 = factory.getVariable(1,"TestInput4",DataType.NUM,1);

        Variable v_out_1 = factory.getVariable(2,"TestOutput2",DataType.NUM,1);
        sim2.getInputs().add(v_in_1);
        sim2.getInputs().add(v_in_2);
        sim2.getInputs().add(v_in_3);
        sim2.getOutputs().add(v_out_1);
        Step s2 = new Step(2,sim2);


        csim.getInputs().add(v_in);
        csim.getOutputs().add(v_out_1);





        CompositeStepMapping mapping = new CompositeStepMapping(csim,s1,s2);

        Assert.assertTrue(csim.getStepMapping().contains(mapping));

        try {
            mapping.addLink(v_in,v_in_1);
            Assert.fail();
        } catch (SimulationCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            mapping.addLink(v_out,v_in_1);
            Assert.fail();
        } catch (SimulationCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            mapping.addLink(v_out,v_in_2);
            Assert.fail();
        } catch (SimulationCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        mapping.addLink(v_out,v_in_3);


        CompositeStepMapping mapping2 = new CompositeStepMapping(csim,null,s1);
        mapping2.addLink(v_in,v_in);

        CompositeStepMapping mapping3 = new CompositeStepMapping(csim,s2,null);
        mapping3.addLink(v_out_1,v_out_1);

    }

     @Test
    public void testRun_1() throws SimulationException {
       CompositeSimulation csim = new CompositeSimulation();




        DefaultSimulation sim1 =  SimulationMockFactory.configurePassThruStrategy(new DefaultSimulation());

        Variable v_in = factory.getVariable(1,"TestInput1",DataType.NUM,1);
        Variable v_out = factory.getVariable(1,"TestOutput1",DataType.NUM,1);
        sim1.getInputs().add(v_in);
        sim1.getOutputs().add(v_out);
        Step s1 = new Step(1,sim1);



        DefaultSimulation sim2 = SimulationMockFactory.configurePassThruStrategy(new DefaultSimulation());
        Variable v_in_3 = factory.getVariable(1,"TestInput4",DataType.NUM,1);
        Variable v_out_1 = factory.getVariable(2,"TestOutput2",DataType.NUM,1);
        sim2.getInputs().add(v_in_3);
        sim2.getOutputs().add(v_out_1);
        Step s2 = new Step(2,sim2);


        csim.getInputs().add(v_in);
        csim.getOutputs().add(v_out_1);
        CompositeStepMapping mapping = new CompositeStepMapping(csim,s1,s2);
        mapping.addLink(v_out,v_in_3);


        CompositeStepMapping mapping2 = new CompositeStepMapping(csim,null,s1);
        mapping2.addLink(v_in,v_in);

        CompositeStepMapping mapping3 = new CompositeStepMapping(csim,s2,null);
        mapping3.addLink(v_out_1,v_out_1);


        Tuple t = new Tuple();
        t.setValue_("4;");
        t.setVar(v_in);

        DefaultScenario scenario = (DefaultScenario) csim.run(Collections.singletonList(t));
        Tuple tout = scenario.getVariableValue(v_out_1);

        Assert.assertArrayEquals(tout.getValues(),new String[] {"4","4"});


    }
}
