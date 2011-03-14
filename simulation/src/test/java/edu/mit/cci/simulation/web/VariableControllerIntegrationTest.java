package edu.mit.cci.simulation.web;




import edu.mit.cci.simulation.model.DataType;
import edu.mit.cci.simulation.model.Variable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.springframework.test.web.ModelAndViewAssert.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/14/11
 * Time: 11:57 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/META-INF/spring/applicationContext.xml","classpath:/webmvc-test-config.xml"})
public class VariableControllerIntegrationTest {

     @Inject
    private ApplicationContext applicationContext;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerAdapter handlerAdapter;

    @Before
    public void setUp() throws Exception {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        Map beans = applicationContext.getBeansOfType(AnnotationMethodHandlerAdapter.class);
        //this is pretty goofy, I'm not sure how to get the correct handler
        this.handlerAdapter = (HandlerAdapter) beans.values().toArray()[0];
    }

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final HandlerMapping handlerMapping = applicationContext.getBean(DefaultAnnotationHandlerMapping.class);
        final HandlerExecutionChain handler = handlerMapping.getHandler(request);
        Assert.assertNotNull("No handler found for request, check you request mapping", handler);

        final Object controller = handler.getHandler();
        // if you want to override any injected attributes do it here

        final HandlerInterceptor[] interceptors =
            handlerMapping.getHandler(request).getInterceptors();
        for (HandlerInterceptor interceptor : interceptors) {
            final boolean carryOn = interceptor.preHandle(request, response, controller);
            if (!carryOn) {
                return null;
            }
        }

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        return mav;
    }

    @Test
    public void testDoSomething() throws Exception {
        String expect = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><List><Variable Id=\"1\"><Name>Test</Name><Description>Test</Description><Arity>1</Arity><DataType>NUM</DataType><Precision>1</Precision></Variable></List>";

        Variable v = new Variable();
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
        Assert.assertEquals(expect, response.getContentAsString());

    }
}
