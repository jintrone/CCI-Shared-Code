package edu.mit.cci.simulation.jaxb;

import javax.xml.bind.JAXBException;

/**
 * User: jintrone
 * Date: 3/18/11
 * Time: 4:47 PM
 */
public interface JaxbReferenceResolver {

    public Object resolve(String id, String type) throws JAXBException;

    public static interface Factory {

        public JaxbReferenceResolver instance();
    }
}
