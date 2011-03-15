package edu.mit.cci.simulation.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name="Scenario")
@XmlAccessorType(XmlAccessType.NONE)
public class DefaultScenario implements Scenario {

     public Tuple getVariableValue(Variable v) {
        for (Tuple t:values_) {
          if (t.getVar().equals(v)) {
              return t;
          }
        }
         return null;
    }

    @XmlIDREF
     @ManyToOne
    private DefaultSimulation simulation;


    @XmlElement(name="Tuples")
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tuple> values_ = new HashSet<Tuple>();

    @XmlElement(name="Created")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date created;

    @XmlAttribute(name="Id")
    @XmlID
    public String getIdAsString() {
        return ""+getId();
    }




}
