package edu.mit.cci.simulation.client;

import edu.mit.cci.simulation.client.model.impl.ClientScenario;


import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlJavaTypeAdapter(ClientScenario.Adapter.class)
public interface Scenario extends HasId {

    public Long getId();

    public String getAuthor();
    public void setAuthor(String u);

    public Date getCreation();
    public void setCreation(Date d);

    public Simulation getSimulation();
    public void setSimulation(Simulation s);

    public String getDescription();
    public void setDescription(String desc);

    public List<Variable> getInputSet();
    public List<Variable> getOutputSet();

    public List<Variable> getCombinedOutputs();

    public void addToInput(Variable v);
    public void addToOutput(Variable v);

    public void setName(String name);
    public String getName();

    public void setState(EntityState name);
    public EntityState getState();

}