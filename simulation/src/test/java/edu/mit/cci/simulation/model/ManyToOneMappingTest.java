package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: jintrone
 * Date: 3/2/11
 * Time: 10:30 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ManyToOneMappingTest {

    @Test
    public void testSum() throws SimulationValidationException {

        Tuple t = new Tuple(new Variable("Test","Test",3,0,0d,10d));
        t.setValues(new String[]{"1", "2", "3"});
        String val = ManyToOneMapping.SUM.reduce(t.getValues());
        Assert.assertEquals(Double.valueOf("6"), Double.valueOf(val));
    }

     @Test
    public void testMedian_odd() throws SimulationValidationException {
        Tuple t = new Tuple(new Variable("Test","Test",3,1,0d,10d));
        t.setValues(new String[]{"1", "2", "3"});
        String val = ManyToOneMapping.MEDIAN.reduce(t.getValues());
        Assert.assertEquals("2.0", val);

    }

     @Test
    public void testMedian_even() throws SimulationValidationException {
        Tuple t = new Tuple(new Variable("Test","Test",4,1,0d,10d));
        t.setValues(new String[]{"1", "2", "3", "4"});
        String val = ManyToOneMapping.MEDIAN.reduce(t.getValues());
        Assert.assertEquals("2.0", val);

    }

     @Test
    public void testFirst() throws SimulationValidationException {
        Tuple t = new Tuple(new Variable("Test","Test",4,0,0d,10d));
        t.setValues(new String[]{"1", "2", "3", "4"});
        String val = ManyToOneMapping.FIRST.reduce(t.getValues());
        Assert.assertEquals("1", val);

    }

     @Test
    public void testLast() throws SimulationValidationException {
        Tuple t = new Tuple(new Variable("Test","Test",4,0,0d,10d));
        t.setValues(new String[]{"1", "2", "3", "4"});
        String val = ManyToOneMapping.LAST.reduce(t.getValues());
        Assert.assertEquals("4",val);

    }



}
