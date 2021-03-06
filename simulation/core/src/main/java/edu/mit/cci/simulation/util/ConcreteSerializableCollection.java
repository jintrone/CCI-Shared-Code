package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultScenario;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: jintrone
 * Date: 3/14/11
 * Time: 9:17 AM
 */
@XmlRootElement(name="List")
@XmlAccessorType(XmlAccessType.NONE)
public class ConcreteSerializableCollection {

    @XmlElements({
            @XmlElement(name="DefaultVariable", type=DefaultVariable.class),
            @XmlElement(name="Simulation", type=DefaultSimulation.class),
            @XmlElement(name="Scenario", type= DefaultScenario.class)
    })
    public final List bucket = new ArrayList();

    public ConcreteSerializableCollection() {}

    public ConcreteSerializableCollection(Collection c) {
        bucket.addAll(c);
    }

    public void add(Object o) {
        if (o instanceof Collection) {
            bucket.addAll((Collection)o);
        } else {
            bucket.add(o);
        }
    }


}
