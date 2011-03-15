package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.*;
import org.codehaus.plexus.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jintrone
 * Date: 3/8/11
 * Time: 2:49 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ExcelRunnerStrategyTest {

    public byte[] getTestFileBytes() throws IOException {
        InputStream infile = getClass().getClassLoader().getResourceAsStream("test_data.xls");
        byte[] b = IOUtil.toByteArray(infile);
        Assert.assertTrue(b.length > 0);
        return b;

    }

    @Test
    public void testReadExcelFile() throws Exception {
        SimulationMockFactory factory = new SimulationMockFactory();
        DefaultSimulation sim = factory.getExcelBasedSimulation(getTestFileBytes());

        String[] dateinput = new String[]{"2000", "2010", "2020", "2030", "2040", "2050", "2060", "2070", "2080", "2090", "2100"};
        String[] emissions = new String[]{"1.7", "1.7", "1.7", "1.7", "1.7", "1.7", "1.7", "1.7", "1.7", "1.7", "1.7"};
        String[] expect = new String[]{"0","0","0.00","0.00","0.00","0.00","0.00","-0.10","-0.53","-0.91","-1.04"};

        List<Tuple> inputs = new ArrayList<Tuple>();

        for (Variable v : sim.getInputs()) {

            Tuple t = new Tuple(v);
            t.persist();
            if (v.getName().equals("Year")) {
                t.setValues(dateinput);

            } else {
                t.setValues(emissions);

            }
            inputs.add(t);

        }

        DefaultScenario scenario = (DefaultScenario) sim.run(inputs);
        Tuple t = scenario.getVariableValue(sim.getOutputs().iterator().next());
        System.out.println(Arrays.toString(t.getValues()));
        Assert.assertArrayEquals(expect, t.getValues());


    }
}
