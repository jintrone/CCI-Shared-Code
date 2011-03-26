package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
public class CompositeStepMapping {


    @ManyToOne
    private CompositeSimulation parentsim;


    @ManyToOne
    private Step fromStep;

    @ManyToOne
    private Step toStep;


    @Transient
    private Set<Variable> fromVars = null;

    @Transient
    private Set<Variable> toVars = null;


    @ManyToMany(targetEntity = VariableList.class)
    @JoinTable(name = "STEP_VAR_TO_VAR")
    private Map<DefaultVariable, VariableList> mapping = new HashMap<DefaultVariable, VariableList>();


    public CompositeStepMapping() {

    }

    public CompositeStepMapping(CompositeSimulation csim, Step s1, Step s2) throws SimulationCreationException {
        this.parentsim = csim;

        if (s1 != null && s2 != null && s1.getOrder_() >= s2.getOrder_()) {
            throw new SimulationCreationException("Mappings can only be between steps that are strictly ordered");
        }

        if ((s1 != null && !csim.getSteps().contains(s1)) || (s2 != null && !csim.getSteps().contains(s2))) {
            throw new SimulationCreationException("Mappings can only be established between steps in the designated parent simulation");
        }

        setFromStep(s1);
        setToStep(s2);
        this.parentsim.getStepMapping().add(this);
    }


//    private void put(DefaultVariable from, DefaultVariable to) {
//        if (!mapping.containsKey())
//    }

    public void addLink(Variable fromVar, Variable toVar) throws SimulationCreationException {

        if (!getFromVars().contains(fromVar) || !getToVars().contains(toVar)) {
            throw new SimulationCreationException("From and to variables must correspond to the steps they connect");
        } else if (fromVar.getArity().intValue() != toVar.getArity() || !(fromVar.getDataType().equals(toVar.getDataType()))) {
            throw new SimulationCreationException("From and to variables must have same arity and datatype");

        } else {
            put((DefaultVariable) fromVar, (DefaultVariable) toVar);

        }

    }

    private void put(DefaultVariable from, DefaultVariable to) {
        VariableList list = mapping.get(from);
        if (list == null) {
            list = new VariableList();
            list.persist();
            mapping.put(from, list);

        }
        list.getVariables().add(to);
    }

    public Set<Variable> getFromVars() {
        if (parentsim==null) throw new RuntimeException("Parent simulation for step mapping should never be null");
        if (fromVars == null) {
            fromVars = new HashSet<Variable>();
            if (getFromStep() == null) {
                fromVars.addAll(parentsim.getInputs());
            } else {
                for (DefaultSimulation s : getFromStep().getSimulations()) {
                    fromVars.addAll(s.getOutputs());
                }
            }
        }
        return this.fromVars;
    }

    public Set<Variable> getToVars() {
        if (parentsim==null) throw new RuntimeException("Parent simulation for step mapping should never be null");
        if (toVars == null) {
            toVars = new HashSet<Variable>();
            if (getToStep() == null) {
                toVars.addAll(parentsim.getOutputs());
            } else {
                for (DefaultSimulation s : toStep.getSimulations()) {
                    toVars.addAll(s.getInputs());
                }
            }
        }
        return this.toVars;
    }


    private void setFromStep(Step from) {
        this.fromStep = from;
        fromVars = null;

    }

    private void setToStep(Step to) {
        this.toStep = to;
        toVars = null;
    }


}
