package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationJAXBAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@XmlJavaTypeAdapter(SimulationJAXBAdapter.class)
@XmlRootElement
public interface Simulation {

    public String getName();
    public void setName(String name);

    public void setDescription(String desc);
    public String getDescription();

    public void setSimulationVersion(Long l);
    public Long getSimulationVersion();

    public void setCreated(Date created);
    public Date getCreated();


    public Set<Variable> getInputs();
    public  void setInputs(Set<Variable> i);

    public Set<Variable> getOutputs();
    public  void setOutputs(Set<Variable> o);

    public String getUrl();
    public void setUrl(String str);

    public Scenario run(List<Tuple> siminputs) throws SimulationException;

    public String getIdAsString();

    public Long getId();


}
