package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationValidationException;
import edu.mit.cci.simulation.util.U;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


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
        DefaultVariable v = new DefaultVariable();
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
        DefaultVariable v = new DefaultVariable();
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
        DefaultVariable v = new DefaultVariable();
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

    @Test
    public void updateStatus() {
        Map<Integer,TupleStatus> map = new HashMap<Integer,TupleStatus>();
        map.put(2,TupleStatus.ERR_OOB);
        map.put(1,TupleStatus.ERR_CALC);
        String[] test = new String[] {"2","3","4",null,"&;&;><","<ERR_OOB/>"};
        String[] expect = new String[] {"2",null,null,null,"&;&;><","<ERR_OOB/>"};
        String str = U.escape(test, map);

        System.err.println(str);
       Map<Integer,TupleStatus> rmap = new HashMap<Integer,TupleStatus>();
        String[] result = U.unescape(str,rmap, null);
        Assert.assertArrayEquals(expect,result);
        Assert.assertEquals(map,rmap);
    }

    @Test
    public void checkErrorHandling() throws SimulationValidationException {
        Tuple t = new Tuple(new DefaultVariable("Test","test",4,0,0d,3d));
        t.setValues(new String[] {"4","-1","2","2"});
        String[] expect = new String[] {null,null,null,"2"};
        t.setStatus(2,TupleStatus.ERR_CALC);
        Assert.assertEquals(t.getStatus(0),TupleStatus.ERR_OOB);
        Assert.assertEquals(t.getStatus(1),TupleStatus.ERR_OOB);
        Assert.assertEquals(t.getStatus(2),TupleStatus.ERR_CALC);
        Assert.assertArrayEquals(expect,t.getValues());


    }


}
