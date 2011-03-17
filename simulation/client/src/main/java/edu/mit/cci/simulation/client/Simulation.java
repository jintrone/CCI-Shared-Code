package edu.mit.cci.simulation.client;

import edu.mit.cci.simulation.client.model.Updateable;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;

@XmlJavaTypeAdapter(ClientSimulation.Adapter.class)
public interface Simulation extends HasId, Updateable {

    public Long getId();

    public String getDescription();
    public void setDescription(String description);

    public URL getURL();
    public void setURL(String url);
    public void setURL(URL url);


    public Date getCreation();
    public void setCreation(Date d);

    public List<MetaData> getInputs();
    public void addInput(MetaData md);

    public List<MetaData> getOutputs();
    public void addOutput(MetaData md);

    public List<MetaData> getCombinedOutputs();

    public String getName();
    public void setName(String name);

    public void setState(EntityState name);
    public EntityState getState();

    public void setType(String type);
    public String getType();

    public Set<Simulation> getChildren();

    //public boolean isComposite();

}