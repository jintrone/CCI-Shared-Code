package edu.mit.cci.simulation.web;


import edu.mit.cci.simulation.model.DataType;
import edu.mit.cci.simulation.model.DefaultScenario;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.util.ConcreteSerializableCollection;
import edu.mit.cci.simulation.util.SpringControllerEnvironment;
import edu.mit.cci.simulation.util.U;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/14/11
 * Time: 11:57 AM
 */
public class VariableControllerIntegrationTest extends SpringControllerEnvironment {

    private static Logger log = Logger.getLogger(VariableControllerIntegrationTest.class);

    @Test
    public void testVariables() throws Exception {
        String expect = "<DefaultVariable Id=\"1\"><Name>Test</Name><Description>Test</Description><Arity>1</Arity><DataType>NUM</DataType><Precision>1</Precision></DefaultVariable>";

        DefaultVariable v = new DefaultVariable();
        v.setDataType(DataType.NUM);
        v.setPrecision_(1);
        v.setName("Test");
        v.setDescription("Test");
        v.setArity(1);
        v.persist();

        request.setRequestURI("/variables/");
        request.addHeader("accept","text/xml");
        request.setMethod("GET");
        final ModelAndView mav = handle(request, response);
        log.info(response.getContentAsString());
        Assert.assertTrue(response.getContentAsString().contains(expect));

    }

//    @Test
//    @Transactional
//    public void testSimulations() throws Exception {
//        String expect = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><List><DefaultVariable Id=\"1\"><Name>Test</Name><Description>Test</Description><Arity>1</Arity><DataType>NUM</DataType><Precision>1</Precision></DefaultVariable></List>";
//
//
//
//        request.setRequestURI("/defaultsimulations");
//        request.addHeader("accept","text/xml");
//        request.setMethod("GET");
//        final ModelAndView mav = handle(request, response);
//        log.info(response.getContentAsString());
//        Assert.assertEquals(expect, response.getContentAsString());
//
//    }
//
//    @Test
//    @Transactional
//    public void testScenarios() throws Exception {
//        String expect = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><List><DefaultVariable Id=\"1\"><Name>Test</Name><Description>Test</Description><Arity>1</Arity><DataType>NUM</DataType><Precision>1</Precision></DefaultVariable></List>";
//
//
//        request.setRequestURI("/defaultscenarios/");
//        request.addHeader("accept","text/xml");
//        request.setMethod("GET");
//        final ModelAndView mav = handle(request, response);
//        log.info(response.getContentAsString());
//        Assert.assertEquals(expect, response.getContentAsString());
//
//    }
//
//


}
