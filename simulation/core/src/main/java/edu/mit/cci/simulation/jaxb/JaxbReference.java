package edu.mit.cci.simulation.jaxb;

import edu.mit.cci.simulation.model.Scenario;
import edu.mit.cci.simulation.model.Simulation;
import edu.mit.cci.simulation.model.Variable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Implementing our own impoverished reference scheme due to bugs in JAXB IDresolver [JAXB-546]
 * and lack of known solution for using custom collections with JPA annotations in Hibernate
 *
 */


@XmlRootElement(name = "reference")
public class JaxbReference {
    @XmlAttribute
    public String type = null;
    @XmlAttribute
    public String id = null;


    public JaxbReference() {

    }

    public JaxbReference(Object node) {
        if (node == null) return;
        if (node instanceof Simulation) {
            type = "Simulation";
            this.id = ((Simulation) node).getId() + "";
        } else if (node instanceof Scenario) {
            type = "Scenario";
            this.id = ((Scenario) node).getId() + "";
        } else if (node instanceof Variable) {
            type = "Variable";
            this.id = ((Variable) node).getId() + "";
        }
    }


    public static class Adapter extends
            XmlAdapter<JaxbReference, Object> {

        @Override
        public JaxbReference marshal(Object arg0) throws Exception {
            return new JaxbReference(arg0);
        }

        @Override
        public Object unmarshal(JaxbReference arg0) throws Exception {
           JaxbReferenceResolver resolver = JaxbUtils.getResolver();
           if (resolver!=null) {
               return resolver.resolve(arg0.id,arg0.type);
           } else {
               return arg0;
           }
        }
    }
}


