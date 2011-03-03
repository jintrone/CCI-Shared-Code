package edu.mit.cci.simulation.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class MappedSimulationTest {


    SimulationMockFactory mock = new SimulationMockFactory();

    @Test
    public void testOneToOne_SubSelect() {

        DefaultSimulation sim = mock.getScalarSimulation(0,13,0);


        MappedSimulation msim = mock.getMappedSimulation(0,sim,123,10,null);
        Set<Variable> inputs = msim.getInputs();
        Set<Variable> outputs = msim.getOutputs();

        Assert.assertEquals(1, inputs.size());
        Assert.assertEquals(1,outputs.size());

        Assert.assertEquals(123,  inputs.iterator().next().getArity().intValue());
        Assert.assertEquals(12,  outputs.iterator().next().getArity().intValue());
    }

    @Test
    public void testOneToOne_OneToOne_Index() {

        DefaultSimulation sim = mock.getScalarSimulation(0,13,0);
        Variable idx = new Variable();
        idx.setArity(1);
        idx.setDataType(DataType.NUM);
        idx.setName("Index");
        idx.persist();
        sim.getInputs().add(idx);


        MappedSimulation msim = mock.getMappedSimulation(0,sim,123,1,null);
        msim.setIndexingVariable(idx);
        Set<Variable> inputs = msim.getInputs();
        Set<Variable> outputs = msim.getOutputs();

        Assert.assertEquals(2,inputs.size());
        Assert.assertEquals(1,outputs.size());


        Assert.assertEquals(123,  inputs.iterator().next().getArity().intValue());
        Assert.assertEquals(123,  inputs.iterator().next().getArity().intValue());

        Variable output = outputs.iterator().next();
        Assert.assertEquals(123, output.getArity().intValue());
        Variable v = output.getIndexingVariable();
        Assert.assertEquals(v.getName(),idx.getName());
        Assert.assertEquals(123,v.getArity().intValue());
    }

    @Test
    public void testRun_oneToOne() throws SimulationException {
       DefaultSimulation sim = mock.getScalarSimulation(0,13,0);
        Variable idx = new Variable();
        idx.setArity(1);
        idx.setDataType(DataType.NUM);
        idx.setName("Index");
        idx.persist();
        sim.getInputs().add(idx);


        MappedSimulation msim = mock.getMappedSimulation(0,sim,123,1,null);
        msim.setIndexingVariable(idx);
        Set<Variable> inputs = msim.getInputs();
        List<Tuple> params = new ArrayList<Tuple>();

        for (Variable v:inputs) {
            String[] x = new String[v.getArity()];
            Arrays.fill(x,"10");
            Tuple t = new Tuple();
            t.setValues(x);
            t.setVar(v);
            params.add(t);
        }

        Scenario s  = msim.run(params);
        for (Variable v:msim.getInputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("10",t.getValues()[0]);
        }

        for (Variable v:msim.getOutputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("13",t.getValues()[0]);


        }



    }

        @Test
    public void testRun_oneToOne_subSelect() throws SimulationException {
       DefaultSimulation sim = mock.getScalarSimulation(0,13,0);
        Variable idx = new Variable();
        idx.setArity(1);
        idx.setDataType(DataType.NUM);
        idx.setName("Index");
        idx.persist();
        sim.getInputs().add(idx);


        MappedSimulation msim = mock.getMappedSimulation(0,sim,123,1,null);
            msim.setSamplingFrequency(10);
        msim.setIndexingVariable(idx);
        Set<Variable> inputs = msim.getInputs();
        List<Tuple> params = new ArrayList<Tuple>();

        for (Variable v:inputs) {
            String[] x = new String[v.getArity()];
            Arrays.fill(x,"10");
            Tuple t = new Tuple();
            t.setValues(x);
            t.setVar(v);
            params.add(t);
        }

        Scenario s  = msim.run(params);
        for (Variable v:msim.getInputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),123);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("10",t.getValues()[0]);
        }

        for (Variable v:msim.getOutputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),13);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("13",t.getValues()[0]);
        }



    }

        @Test
    public void testRun_oneToOne_subSelect_reduce() throws SimulationException {
       DefaultSimulation sim = mock.getScalarSimulation(0,13,0);
        Variable idx = new Variable();
        idx.setArity(1);
        idx.setDataType(DataType.NUM);
        idx.setName("Index");
        idx.persist();
        sim.getInputs().add(idx);


        MappedSimulation msim = mock.getMappedSimulation(0,sim,123,1,null);
            msim.setSamplingFrequency(10);
            msim.setManyToOne(ManyToOneMapping.SUM);
        msim.setIndexingVariable(idx);
        Set<Variable> inputs = msim.getInputs();
        List<Tuple> params = new ArrayList<Tuple>();

        for (Variable v:inputs) {
            String[] x = new String[v.getArity()];
            Arrays.fill(x,"10");
            Tuple t = new Tuple();
            t.setValues(x);
            t.setVar(v);
            params.add(t);
        }

        Scenario s  = msim.run(params);
        for (Variable v:msim.getInputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),123);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("10",t.getValues()[0]);
        }

        for (Variable v:msim.getOutputs()) {
            Tuple t = s.getVariableValue(v);
            Assert.assertNotNull(t);
            Assert.assertEquals(v.getArity().intValue(),1);
            Assert.assertEquals(v.getArity().intValue(),t.getValues().length);
            Assert.assertEquals("169.0",t.getValues()[0]);
        }



    }



}
