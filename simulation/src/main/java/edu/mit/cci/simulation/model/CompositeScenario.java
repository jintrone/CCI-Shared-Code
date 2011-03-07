package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashMap;
import java.util.Map;

@RooJavaBean
@RooToString
@RooEntity
public class CompositeScenario extends DefaultScenario {

    @ManyToMany @JoinTable(name="STEP_SCENARIO")
    private Map<Step,ScenarioList> childScenarios = new HashMap<Step,ScenarioList>();

    private int lastStep;


    public void addToStep(Step s, DefaultScenario scenario) {
        if (!childScenarios.containsKey(s)) {
            ScenarioList list = new ScenarioList();
            list.persist();
            childScenarios.put(s,list);

        }
            childScenarios.get(s).getScenarios().add(scenario);

    }

    public void clearStep(Step s) {
        if (childScenarios.containsKey(s)) {
            childScenarios.get(s).getScenarios().clear();
        }
    }

}
