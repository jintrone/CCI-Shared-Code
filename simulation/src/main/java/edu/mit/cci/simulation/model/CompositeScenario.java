package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Map;

@RooJavaBean
@RooToString
@RooEntity
public class CompositeScenario extends DefaultScenario {

    @ManyToMany @JoinTable(name="STEP_SCENARIO")
    private Map<Step,DefaultScenario> childScenarios;

    private int lastStep;
}
