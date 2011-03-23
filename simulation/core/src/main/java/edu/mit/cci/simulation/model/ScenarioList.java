package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
public class ScenarioList {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<DefaultScenario> scenarios = new HashSet<DefaultScenario>();

    @ElementCollection
    private Set<DataType> testField = new HashSet<DataType>();
}
