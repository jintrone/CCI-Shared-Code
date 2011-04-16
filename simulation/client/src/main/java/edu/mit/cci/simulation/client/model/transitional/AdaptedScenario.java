package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.Variable;
import edu.mit.cci.simulation.client.comm.RepositoryManager;
import edu.mit.cci.simulation.model.Tuple;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 4:31 PM
 */
public class AdaptedScenario extends AdaptedObject<edu.mit.cci.simulation.model.Scenario> implements Scenario {

    Simulation sim = null;
    List<Variable> inputs = null;
    List<Variable> outputs = null;

    public AdaptedScenario(edu.mit.cci.simulation.model.Scenario o, RepositoryManager manager) {
        super(o,manager);
    }

    @Override
    public Long getId() {
        return model().getId();
    }

    @Override
    public String getAuthor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAuthor(String u) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getCreation() {
        return model().getCreated();
    }

    @Override
    public void setCreation(Date d) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Simulation getSimulation() {
        if (sim == null) {
            sim = (Simulation) manager().getAdaptor(model().getSimulation());
        }
        return sim;
    }

    @Override
    public void setSimulation(Simulation s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDescription(String desc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Variable> getInputSet() {
       if (inputs == null) {
           Set<Tuple> vals = new HashSet<Tuple>(model().getValues_());
           filterTuples(vals, model().getSimulation().getInputs());
           inputs = processTupleList(vals);
       }
        return inputs;
    }

    private static void filterTuples(Set<Tuple> vals, Set<edu.mit.cci.simulation.model.Variable> vars) {
        for (Iterator<Tuple> it = vals.iterator();it.hasNext();) {
           if (!vars.contains(it.next().getVar())) {
               it.remove();
            }
        }
    }

    private List<Variable> processTupleList(Set<edu.mit.cci.simulation.model.Tuple> tuples) {
        List<Variable> result = new ArrayList<Variable>();
        for (Tuple t:tuples) {
            result.add(new AdaptedVariable(t,tuples,manager()));
        }
        return result;

    }

    @Override
    public List<Variable> getOutputSet() {
       if (outputs == null) {
           Set<Tuple> vals = new HashSet<Tuple>(model().getValues_());
           filterTuples(vals,model().getSimulation().getOutputs());
           outputs = processTupleList(vals);
       }
        return outputs;
    }

    @Override
    public List<Variable> getCombinedOutputs() {
        return getOutputSet();
    }

    @Override
    public void addToInput(Variable v) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addToOutput(Variable v) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setName(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setState(EntityState name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EntityState getState() {
        return EntityState.PUBLIC;
    }
    
    public Variable getVariableForInternalname(String internalname) {
        if (internalname==null) return null;
        for (Variable input:getInputSet()) {
            if (internalname.equals(input.getMetaData().getInternalName())) {
                return input;
            }
        }
        for (Variable output:getOutputSet()) {
            if (internalname.equals(output.getMetaData().getInternalName())) {
                return output;
            }
        }
        return null;
    }
}
