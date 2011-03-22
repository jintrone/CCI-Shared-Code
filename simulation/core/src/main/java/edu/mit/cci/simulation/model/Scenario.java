package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.ScenarioJAXBAdapter;
import edu.mit.cci.simulation.util.VariableJAXBAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

/**
 * User: jintrone
 * Date: 2/10/11
 * Time: 3:07 PM
 */

@XmlJavaTypeAdapter(ScenarioJAXBAdapter.class)
@XmlRootElement
public interface Scenario {

    public Simulation getSimulation();
    public void setSimulation(Simulation sim);

    public Tuple getVariableValue(Variable v);

    public Set<Tuple> getValues_();

    public Date getCreated();
    public void setCreated(Date created);

    public void setId(Long id);
    public Long getId();


    String getName();

    void setName(String name);

    public String getUser();

    public void setUser(String user);
}

