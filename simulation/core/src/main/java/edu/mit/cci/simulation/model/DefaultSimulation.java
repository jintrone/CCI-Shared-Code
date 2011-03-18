package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.Jar;
import org.hibernate.HibernateException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentSet;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.print.DocFlavor;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: jintrone
 * Date: 2/10/11
 * Time: 3:17 PM
 */
@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name="Simulation")
@XmlAccessorType(XmlAccessType.NONE)
public class DefaultSimulation implements Simulation {

    private static Logger log = Logger.getLogger(DefaultSimulation.class);

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @XmlElement(name="Creation")
    private Date created;

    @NotNull
    private Long simulationVersion;

     @XmlElement(name="Description")
    private String description;

    @XmlElement(name="Name")
    private String name;

    @XmlElement(name="Url")
    private String url;

    @ManyToMany(cascade = CascadeType.ALL,targetEntity = DefaultVariable.class)
    private Set<Variable> inputs = new JAXBBug546Set();


    @ManyToMany(cascade = CascadeType.ALL,targetEntity = DefaultVariable.class)
    private Set<Variable> outputs = new JAXBBug546Set();

    @Transient
    private transient RunStrategy runStrategy = new RunStrategy.Post();

    @XmlElementWrapper(name="Outputs")
    @XmlElement(name="DefaultVariable",type = DefaultVariable.class) @XmlIDREF
    private JAXBBug546Set getJAXBOutputs() {
        return (JAXBBug546Set)outputs;
    }

    @XmlElementWrapper(name="Inputs")
    @XmlElement(name="DefaultVariable",type = DefaultVariable.class) @XmlIDREF
        public JAXBBug546Set getJAXBInputs() {
        return (JAXBBug546Set)inputs;
    }

    public Scenario run(List<Tuple> siminputs) throws SimulationException {
        DefaultScenario result = new DefaultScenario();
        result.setSimulation(this);
        Set<Tuple> response = runRaw(siminputs);
        Set<Variable> outputs = new HashSet<Variable>(getOutputs());
        for (Tuple t : response) {
            outputs.remove(t.getVar());
        }
        if (!outputs.isEmpty()) {
            log.warn("Not all outputs were identified, missing: " + outputs);
        }
        result.getValues_().addAll(siminputs);
        result.getValues_().addAll(response);
        result.persist();
        return result;
    }

    @Override
    public void setInputs(Set<Variable> i) {
        this.inputs.clear();
        if (i != null) {
            inputs.addAll(i);
        }
    }

    @Override
    public void setOutputs(Set<Variable> o) {
        this.outputs.clear();
        if (o != null) {
            outputs.addAll(o);
        }
    }

    public Set<Variable> getInputs() {
        return this.inputs;
    }

    public Set<Variable> getOutputs() {
        return this.outputs;
    }

    @Override
    @XmlAttribute(name="Id")
    @XmlID
    public String getIdAsString() {
        return ""+getId();
    }

    /**
     * Runs a simulation and returns a map of output variables to tuple values
     *
     * @param siminputs
     * @return
     * @throws SimulationException
     */
    protected Set<Tuple> runRaw(Collection<Tuple> siminputs) throws SimulationException {
        Set<Variable> mine = new HashSet<Variable>(getInputs());
        Set<Tuple> result = new HashSet<Tuple>();
        Map<String, String> params = new HashMap<String, String>();
        for (Tuple t : siminputs) {
            if (mine.remove(t.getVar())) {
                if (t.getVar().getExternalName()!=null) {
                    params.put(t.getVar().getExternalName()+"",t.getValue_());
                } else {
                    params.put(t.getVar().getId() + "", t.getValue_());
                }
            }
        }
        if (!mine.isEmpty()) {
            throw new SimulationException("Missing input variables: " + mine);
        }
        String response = null;
       response = runStrategy.run(url, params);

        result.addAll(U.parseVariableMap(response, getOutputs()));
        return result;
    }

    public void setRunStrategy(RunStrategy r) {
        this.runStrategy = r;
    }

    public static class JAXBBug546Set extends HashSet<Variable>  {

        public JAXBBug546Set() {
            super();
        }

        public JAXBBug546Set(Set<Variable> other) {
            super(other);
        }


    }
}
