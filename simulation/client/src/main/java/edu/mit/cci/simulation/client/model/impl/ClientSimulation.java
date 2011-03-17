package edu.mit.cci.simulation.client.model.impl;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Simulation;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@XmlRootElement(name="simulation")
public class ClientSimulation implements Simulation {

    private Long id;
    private boolean composite;
    private List<MetaData> inputs = null;
    private String name;
    private List<MetaData> outputs = null;
    private Date creation;
    private EntityState state;
    private String description;
    private Set<Simulation> children = null;
    private URL url;
    private String type;

    private Map<String,String> updates = new HashMap<String,String>();
    private List<String> updateable = new ArrayList<String>(Arrays.asList(new String[] {"url","description","name"}));


    public ClientSimulation() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setComposite(boolean composite) {
        this.composite = composite;
    }

    public void setInputs(List<MetaData> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(List<MetaData> outputs) {
        this.outputs = outputs;
    }

    public void setChildren(List<Simulation> children) {
        this.children = new HashSet<Simulation>(children);

    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @XmlElement(name="description")
    public String getDescription() {
        return description;
    }

    @XmlAttribute(name="id")
    public Long getId() {
        return id;
    }

    @XmlAttribute(name="composite")
        public boolean isComposite() {
         return composite;
        }



    @XmlElementWrapper(name="inputs")
    @XmlElement(name="metadata")
    public List<MetaData> getInputs() {
        if (inputs == null) {
            inputs=  new ArrayList<MetaData>();
        }
        return inputs;
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    @XmlElementWrapper(name="outputs")
    @XmlElement(name="metadata")
    public List<MetaData> getCombinedOutputs() {
        return getOutputs();
    }

    @XmlElementWrapper(name="children")
    @XmlElement(name="childsim")
    @XmlIDREF
    public Set<Simulation> getChildren() {
        if (children ==null) {
            children = new HashSet<Simulation>();

        }
        return children;
    }

    public List<MetaData> getOutputs() {
       if (outputs == null) {
           outputs = new ArrayList<MetaData>();
       }
       
       
       return outputs;
    }

    @XmlElement(name="url")
    public URL getURL() {
        return url;
    }

    @XmlElement(name="state")
    public EntityState getState() {
        return state;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name="type")
    public String getType() {
       return type;
    }


    @XmlElement(name="creation")
    public Date getCreation() {
        return creation;
    }

    @Override
    public void addInput(MetaData md) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addOutput(MetaData md) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDescription(String description) {
        if (unequal(description,this.description)) {
            if (!updateable.contains("description")) {
                updates.put("description",description==null?"":description);
            } else {
                updateable.remove("description");
            }
        }
        this.description = description;

    }

    @Override
    public void setName(String name) {
        if (unequal(name,this.name)) {
            if (!updateable.contains("name")) {
                updates.put("name",name==null?"":name);
            }  else {
                updateable.remove("name");
            }
        }
        this.name = name;

    }

    @Override
    public void setURL(String url) {
        if (unequal(url,this.url==null?"":this.url.toExternalForm())) {
            if (!updateable.contains("url")) {
                updates.put("url",url);
            } else {
                updateable.remove("url");
            }
        }
        if (url==null) this.url = null;
        else {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        }

    }

    @Override
    public void setURL(URL url) {
        if (unequal(url,this.url)) {
            if (!updateable.contains("url")) {
                updates.put("url",url!=null?url.toExternalForm():"");
            } else {
               updateable.remove("url"); 
            }
        }
        this.url = url;

    }

    @Override
    public Map<String, String> getUpdate() {
        return updates;
    }


    @Override
    public boolean isDirty() {
        return updates.size() > 0;
    }


    public static class Adapter extends XmlAdapter<ClientSimulation,Simulation> {

        @Override
        public ClientSimulation marshal(Simulation v) throws Exception {
            return null;
        }

        @Override
        public Simulation unmarshal(ClientSimulation v) throws Exception {
            return v;
        }

    }


    @Override
    public void setState(EntityState name) {
        this.state = name;

    }



    @Override
    public void setCreation(Date d) {
       this.creation = d;

    }

    private static boolean unequal(Object a, Object b) {
        if ((a != null && b ==null) || (a==null && b!=null)) return true;
        else return ((a!=null && !a.equals(b)) || (b!=null && !b.equals(a)));
    }

    public boolean equals(Object o) {
       return (o instanceof Simulation && ((Simulation)o).getId().equals(getId()));
    }

    public int hashCode() {
        return (Simulation.class.hashCode() * getId().hashCode())%13;
    }

    public static Map<String,String> parseTypes(Simulation sim) {
        if (sim.getType() == null) return Collections.emptyMap();
        Map<String,String> result = new HashMap<String,String>();
        for (String type:sim.getType().split(";")) {
            String[] kv = type.split("=");
            if (kv.length>1) {
                result.put(kv[0],kv[1]);
            }
        }
        return result;
    }


}