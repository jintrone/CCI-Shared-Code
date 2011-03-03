package edu.mit.cci.simulation.model;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
* User: jintrone
* Date: 2/28/11
* Time: 12:52 PM
*/
public class DelegatingSim extends DefaultSimulation {

    public DefaultSimulation delegate;

    public DelegatingSim(DefaultSimulation delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public Date getCreated() {
        return delegate.getCreated();
    }

    @Override
    public void setCreated(Date created) {
        delegate.setCreated(created);
    }

    @Override
    public Long getSimulationVersion() {
        return delegate.getSimulationVersion();
    }

    @Override
    public void setSimulationVersion(Long simulationVersion) {
        delegate.setSimulationVersion(simulationVersion);
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public Long getId() {
        return delegate.getId();
    }

    @Override
    public String getUrl() {
        return delegate.getUrl();
    }

    @Override
    public void setId(Long id) {
        delegate.setId(id);
    }

    @Override
    public void setUrl(String url) {
        delegate.setUrl(url);
    }

    @Override
    public Integer getVersion() {
        return delegate.getVersion();
    }

    @Override
    public Scenario run(List<Tuple> siminputs) throws SimulationException {
        return delegate.run(siminputs);
    }

    @Override
    public Set<Variable> getInputs() {
        return delegate.getInputs();
    }

    @Override
    public void setVersion(Integer version) {
        delegate.setVersion(version);
    }

    @Override
    public void setInputs(Set<Variable> inputs) {
        delegate.setInputs(inputs);
    }

    @Override
    @Transactional
    public void persist() {
        delegate.persist();
    }

    @Override
    public Set<Variable> getOutputs() {
        return delegate.getOutputs();
    }

    @Override
    public void setOutputs(Set<Variable> outputs) {
        delegate.setOutputs(outputs);
    }

    @Override
    @Transactional
    public void remove() {
        delegate.remove();
    }

    @Override
    public Set<Tuple> runRaw(Collection<Tuple> siminputs) throws SimulationException {
        return delegate.runRaw(siminputs);
    }

    @Override
    @Transactional
    public void flush() {
        delegate.flush();
    }

    @Override
    @Transactional
    public DefaultSimulation merge() {
        return delegate.merge();
    }


}
