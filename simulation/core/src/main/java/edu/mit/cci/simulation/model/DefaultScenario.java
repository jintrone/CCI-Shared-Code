package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.jaxb.JaxbReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name = "Scenario")
@XmlAccessorType(XmlAccessType.NONE)
public class DefaultScenario implements Scenario {

    public Tuple getVariableValue(Variable v) {
        for (Tuple t : values_) {
            if (t.getVar().equals(v)) {
                return t;
            }
        }
        return null;
    }

    @ManyToOne(targetEntity = DefaultSimulation.class)
    @XmlJavaTypeAdapter(JaxbReference.Adapter.class)
    private Simulation simulation;


    @XmlElement(name = "Tuples")
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tuple> values_ = new HashSet<Tuple>();

    @XmlElement(name = "Created")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date created;


    public String getIdAsString() {
        return "" + getId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @XmlAttribute(name = "Id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
