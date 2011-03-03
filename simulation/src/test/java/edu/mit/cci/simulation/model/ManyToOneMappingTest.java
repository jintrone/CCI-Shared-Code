package edu.mit.cci.simulation.model;

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
    public void testSum() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"1", "2", "3"});
        ManyToOneMapping.SUM.reduce(t);
        Assert.assertEquals(Double.valueOf("6"), Double.valueOf(t.getValues()[0]));
    }

     @Test
    public void testMedian_odd() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"1", "2", "3"});
        ManyToOneMapping.MEDIAN.reduce(t);
        Assert.assertEquals("2.0", t.getValues()[0]);

    }

     @Test
    public void testMedian_even() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"1", "2", "3", "4"});
        ManyToOneMapping.MEDIAN.reduce(t);
        Assert.assertEquals("2.0", t.getValues()[0]);

    }

     @Test
    public void testFirst() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"1", "2", "3", "4"});
        ManyToOneMapping.FIRST.reduce(t);
        Assert.assertEquals("1", t.getValues()[0]);

    }

     @Test
    public void testLast() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"1", "2", "3", "4"});
        ManyToOneMapping.LAST.reduce(t);
        Assert.assertEquals("4", t.getValues()[0]);

    }
      @Test
    public void testBadInputEmpty() {
        Tuple t = new Tuple();
        t.setValues(new String[]{});
        ManyToOneMapping.FIRST.reduce(t);
        Assert.assertEquals(0, t.getValues().length);

    }
       @Test
    public void testBadInputNull() {
        Tuple t = new Tuple();
        t.setValues(null);
        ManyToOneMapping.FIRST.reduce(t);
        Assert.assertEquals(0, t.getValues().length);

    }
      @Test
    public void testBadInputNonNumeric() {
        Tuple t = new Tuple();
        t.setValues(new String[]{"a"});
        try {
            ManyToOneMapping.SUM.reduce(t);
            Assert.fail();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }
}
