package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.comm.RepositoryManager;
import edu.mit.cci.simulation.model.Variable;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 4:23 PM
 */
public class AdaptedSimulation extends AdaptedObject<edu.mit.cci.simulation.model.Simulation> implements Simulation {


    private List<MetaData> inputs;
    private List<MetaData> outputs;
    private URL[] url=null;
    private Logger logger = Logger.getLogger(AdaptedSimulation.class);

    public AdaptedSimulation(edu.mit.cci.simulation.model.Simulation o, RepositoryManager manager) {
        super(o,manager);

    }

    @Override
    public Long getId() {
       return getProxiedObject().getId();
    }

    @Override
    public String getDescription() {
        return getProxiedObject().getDescription();
    }

    @Override
    public void setDescription(String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public URL getURL() {
      if (url == null || getProxiedObject().getUrl()!=null) {
          try {
              url = new URL[]{new URL(getProxiedObject().getUrl())};
          } catch (MalformedURLException e) {
              logger.warn("Error resolving url");
              url = new URL[0];
          }
      }
        return url[0];
    }

    @Override
    public void setURL(String url) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setURL(URL url) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getCreation() {
        return getProxiedObject().getCreated();
    }

    @Override
    public void setCreation(Date d) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<MetaData> getInputs() {
        if (inputs == null) {
            inputs = new ArrayList<MetaData>();
            for (Variable v:getProxiedObject().getInputs()) {
                inputs.add((MetaData) getManager().getAdaptor(v));
            }
        }
        return inputs;
    }

    @Override
    public void addInput(MetaData md) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<MetaData> getOutputs() {
         if (outputs== null) {
            outputs = new ArrayList<MetaData>();
            for (Variable v:getProxiedObject().getOutputs()) {
                outputs.add((MetaData) getManager().getAdaptor(v));
            }
        }
        return outputs;
    }


    @Override
    public void addOutput(MetaData md) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<MetaData> getCombinedOutputs() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return getProxiedObject().getName();
    }

    @Override
    public void setName(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setState(EntityState name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EntityState getState() {
        return EntityState.PUBLIC;
    }

    @Override
    public void setType(String type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Simulation> getChildren() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getUpdate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDirty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
