package edu.mit.cci.simulation.excel.server;



import org.codehaus.plexus.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: jintrone
 * Date: 3/8/11
 * Time: 3:54 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ExcelSimulationTest {

    public byte[] getTestFileBytes() throws IOException {
        InputStream infile = getClass().getClassLoader().getResourceAsStream("test_data.xls");
        byte[] b = IOUtil.toByteArray(infile);
        Assert.assertTrue(b.length>0);
        return b;

    }

    @Test
    public void testSetGetFile() throws Exception {
        ExcelSimulation sim = new ExcelSimulation();
        sim.setFile(getTestFileBytes());
        sim.persist();


        ExcelSimulation sim2 = ExcelSimulation.findExcelSimulation(sim.getId());
        Assert.assertArrayEquals(sim.getFile(), sim2.getFile());


    }


}
