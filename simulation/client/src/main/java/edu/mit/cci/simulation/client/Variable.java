package edu.mit.cci.simulation.client;

import edu.mit.cci.simulation.client.model.impl.ClientVariable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


@XmlJavaTypeAdapter(ClientVariable.Adapter.class)
public interface Variable extends HasId {

    public Long getId();
    public MetaData getMetaData();
    public void setMetaData(MetaData md);

    public List<Tuple> getValue();
    public void addValue(Tuple t);
}