package edu.mit.cci.simulation.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/18/11
 * Time: 4:11 PM
 */
@XmlRootElement(name="collection")
public class JaxbCollection {

    public Set<JaxbReference> items = new HashSet<JaxbReference>();


    public JaxbCollection() {

    }

    public JaxbCollection(Collection c) {
        for (Object o:c) {
            items.add(new JaxbReference(o));
        }
    }

     public static class Adapter extends
             XmlAdapter<JaxbCollection, Collection> {

        @Override
        public JaxbCollection marshal(Collection arg0) throws Exception {
            return new JaxbCollection(arg0);
        }


        public Collection unmarshal(JaxbCollection arg0) throws Exception {
            Set result = new HashSet();
            JaxbReferenceResolver resolver = JaxbUtils.getResolver();
            if (resolver!=null) {
                for (Object o:arg0.items) {
                    JaxbReference ref = (JaxbReference)o;
                    Object unmarshalled = resolver.resolve(ref.id,ref.type);
                    result.add(unmarshalled);

                }
                return result;
            } else {
                return arg0.items;
            }
        }



     }

}
