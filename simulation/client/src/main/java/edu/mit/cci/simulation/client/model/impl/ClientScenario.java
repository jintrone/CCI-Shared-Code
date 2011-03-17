package edu.mit.cci.simulation.client.model.impl;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.Variable;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name="scenario")
public class ClientScenario implements Scenario {


    private String author;
    private Date creation;
    private String description;
    private Long id;
    private List<Variable> inputs;
    private List<Variable> outputs;
    private Simulation simulation;
    private EntityState state;
    private String name;


    public ClientScenario() {

    }



    @XmlElement(name="authorid")
    public String getAuthor() {
        return author;
    }

    @XmlElement(name="creation")
    public Date getCreation() {
       return creation;
    }

    @XmlElement(name="description")
    public String getDescription() {
        return description;
    }

    @XmlAttribute(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElementWrapper(name="inputs")
    @XmlElement(name="variable")
    public List<Variable> getInputSet() {
        if (inputs == null) {
            inputs = new ArrayList<Variable>();

        }
        return inputs;
    }


    public List<Variable> getOutputSet() {
        if (outputs == null) {
            outputs = new ArrayList<Variable>();
        }
        return outputs;
    }

    @XmlElementWrapper(name="outputs")
    @XmlElement(name="variable")
    public List<Variable> getCombinedOutputs() {
        return getOutputSet();
    }

    @XmlElement(name="simulation")
    @XmlIDREF
    public Simulation getSimulation() {
        return simulation;
    }

    @XmlElement(name="state")
    public EntityState getState() {
        return state;
    }

    @Override
    public void addToInput(Variable v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addToOutput(Variable v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAuthor(String s) {
        this.author = author;

    }

    @Override
    public void setCreation(Date d) {
        this.creation = creation;

    }

    @Override
    public void setDescription(String desc) {
        this.description = desc;

    }

    @Override
    public void setSimulation(Simulation s) {
        this.simulation = s;

    }


    public static class Adapter extends XmlAdapter<ClientScenario,Scenario> {

        @Override
        public ClientScenario marshal(Scenario v) throws Exception {
            return null;
        }

        @Override
        public Scenario unmarshal(ClientScenario v) throws Exception {
            return v;
        }


    }


    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
       this.name = name;

    }



    @Override
    public void setState(EntityState name) {
       this.state = name;

    }

    public boolean equals(Object o) {
       return (o instanceof Scenario && ((Scenario)o).getId().equals(getId()));
    }

    public int hashCode() {
        return (Scenario.class.hashCode() * getId().hashCode())%13;
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