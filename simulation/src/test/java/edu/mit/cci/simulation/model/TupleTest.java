package edu.mit.cci.simulation.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * User: jintrone
 * Date: 3/14/11
 * Time: 9:51 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class TupleTest {

    @Test
    public void testTupleArity() {
        Variable v = new Variable();
        v.setDataType(DataType.NUM);
        v.setArity(1);
        v.setName("Test");
        v.setDescription("Test1");
        v.setMin_(0d);
        v.setMax_(10d);
        v.setPrecision_(0);
        Tuple t = new Tuple();
        t.setVar(v);

        try {
            t.setValues(new String[]{"1", "2"});
            Assert.fail("Should not be able to set invalid arity on tuple");
        } catch (Exception e) {

        }
    }

    @Test
    public void testTupleMinMax() throws Exception {
        Variable v = new Variable();
        v.setDataType(DataType.NUM);
        v.setArity(2);
        v.setName("Test");
        v.setDescription("Test1");
        v.setMin_(0d);
        v.setMax_(10d);
        v.setPrecision_(0);
        Tuple t = new Tuple();
        t.setVar(v);
        t.setValues(new String[]{"1", "20"});
        Assert.assertNull(t.getValues()[1]);
        Assert.assertEquals(TupleStatus.ERR_OOB, t.getStatus(1));
    }

    @Test
    public void testTupleNumericType() {
        Variable v = new Variable();
        v.setDataType(DataType.NUM);
        v.setArity(2);
        v.setName("Test");
        v.setDescription("Test1");
        v.setMin_(0d);
        v.setMax_(10d);
        v.setPrecision_(0);


        Tuple t = new Tuple();
        t.setVar(v);
        try {
            t.setValues(new String[]{"a", "20"});
            Assert.fail("Should not be able to set non-numeric value on numeric tuple");
        } catch(Exception e) {

        }

    }
}
