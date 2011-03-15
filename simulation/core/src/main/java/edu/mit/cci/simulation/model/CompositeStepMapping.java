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
    private final Set<Variable> fromVars = new HashSet<Variable>();

    @Transient
    private final Set<Variable> toVars = new HashSet<Variable>();


    @ManyToMany
    @JoinTable(name="STEP_VAR_TO_VAR")
    private Map<Variable,Variable> mapping = new HashMap<Variable,Variable>();



    public CompositeStepMapping() {

    }

    public CompositeStepMapping(CompositeSimulation csim, Step s1, Step s2) throws SimulationCreationException {
        this.parentsim = csim;

        if (s1!=null && s2!=null && s1.getOrder_()>=s2.getOrder_()) {
            throw new SimulationCreationException("Mappings can only be between steps that are strictly ordered");
        }

        if ((s1!=null && !csim.getSteps().contains(s1)) || (s2!=null && !csim.getSteps().contains(s2))) {
            throw new SimulationCreationException("Mappings can only be established between steps in the designated parent simulation");
        }

        setFromStep(s1);
        setToStep(s2);
        this.parentsim.getStepMapping().add(this);
    }


    public void addLink(Variable fromVar, Variable toVar) throws SimulationCreationException {

        if (!fromVars.contains(fromVar) || !toVars.contains(toVar)) {
            throw new SimulationCreationException("From and to variables must correspond to the steps they connect");
        } else if (fromVar.getArity().intValue()!=toVar.getArity() || !(fromVar.getDataType().equals(toVar.getDataType()))) {
             throw new SimulationCreationException("From and to variables must have same arity and datatype");

        } else {
                mapping.put(fromVar,toVar);

        }

    }



    private void setFromStep(Step from) {
        this.fromStep = from;
        fromVars.clear();
        if (from == null) {
            fromVars.addAll(parentsim.getInputs());
        } else {
           for (DefaultSimulation s:from.getSimulations()) {
              fromVars.addAll(s.getOutputs());
            }
       }
    }

    private void setToStep(Step to) {
        this.toStep = to;
        toVars.clear();
        if (to == null) {
            toVars.addAll(parentsim.getOutputs());
        } else {
           for (DefaultSimulation s:to.getSimulations()) {
              toVars.addAll(s.getInputs());
            }
       }
    }






}
