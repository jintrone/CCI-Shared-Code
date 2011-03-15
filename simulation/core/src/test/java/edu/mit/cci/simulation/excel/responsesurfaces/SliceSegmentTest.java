package edu.mit.cci.simulation.excel.responsesurfaces;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: jintrone
 * Date: 2/25/11
 * Time: 12:34 AM
 */
public class SliceSegmentTest {

    private static Logger log = Logger.getLogger(SliceSegmentTest.class);

    @Test
    public void testInvalid() throws Exception {
        Polynomial p = new Polynomial(new double[] {4,5});
        try {
            SliceSegment<Float,Integer> ss = new SliceSegment<Float, Integer>(4f,3f,100,p);
            Assert.fail("Should not be able to create slice segements with to>from");
        } catch (IllegalArgumentException ex) {
            //ok
        }
    }

    @Test
    public void testValid() throws Exception {
        String expect = "3.0-4.0 : f(x) = +5.0x +4.0";
        Polynomial p = new Polynomial(new double[] {4,5});
        SliceSegment<Float,Integer> ss = new SliceSegment<Float, Integer>(3f,4f,100,p);
        log.info(ss);

        Assert.assertTrue(ss.getIndex()==100);
        Assert.assertEquals(3f,ss.getFromCriterion(),.0001);
        Assert.assertEquals(4f,ss.getToCriterion(),.001);
        Assert.assertEquals(expect,ss.toString());
    }




}
