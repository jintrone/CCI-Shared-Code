package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.Variable;
import edu.mit.cci.simulation.client.comm.RepositoryManager;

import java.util.Date;
import java.util.List;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 4:31 PM
 */
public class AdaptedScenario extends AdaptedObject<edu.mit.cci.simulation.model.Scenario> implements Scenario {
    public AdaptedScenario(edu.mit.cci.simulation.model.Scenario o, RepositoryManager manager) {
        super(o,manager);
    }

    @Override
    public Long getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCreation(Date d) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Simulation getSimulation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Variable> getOutputSet() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Variable> getCombinedOutputs() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
