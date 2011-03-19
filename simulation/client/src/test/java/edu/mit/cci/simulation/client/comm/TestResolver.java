package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.jaxb.JaxbReferenceResolver;

/**
 * User: jintrone
 * Date: 3/18/11
 * Time: 5:17 PM
 */
public class TestResolver implements JaxbReferenceResolver.Factory, JaxbReferenceResolver {


    private static TestResolver instance;


    public Object resolve(String id, String type) {
        return "fooey";
    }


    public JaxbReferenceResolver instance() {
        if (instance == null) {
            instance = new TestResolver();
        }
        return instance;
    }

}
