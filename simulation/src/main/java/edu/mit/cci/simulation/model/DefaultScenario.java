package edu.mit.cci.simulation.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
public class DefaultScenario implements Scenario {

     public Tuple getVariableValue(Variable v) {
        for (Tuple t:values_) {
          if (t.getVar().equals(v)) {
              return t;
          }
        }
         return null;
    }

     @ManyToOne
    private DefaultSimulation simulation;



    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tuple> values_ = new HashSet<Tuple>();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date created;




}
