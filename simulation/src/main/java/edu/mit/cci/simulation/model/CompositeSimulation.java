package edu.mit.cci.simulation.model;

import org.hibernate.annotations.OrderBy;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooEntity
public class CompositeSimulation extends DefaultSimulation {

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy(clause = "order_ ASC")
    private List<Step> steps = new ArrayList<Step>();
}
