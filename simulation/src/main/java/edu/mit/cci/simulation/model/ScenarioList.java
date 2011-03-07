package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.Set;
import edu.mit.cci.simulation.model.DefaultScenario;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class ScenarioList {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<DefaultScenario> scenarios = new HashSet<DefaultScenario>();
}
