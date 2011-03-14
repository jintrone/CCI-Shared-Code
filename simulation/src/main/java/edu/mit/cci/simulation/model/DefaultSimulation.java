package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

    @XmlElement(name="Inputs",type=Variable.class)
    @XmlIDREF
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Variable> inputs = new HashSet<Variable>();

    @XmlElement(name="Outputs",type=Variable.class)
    @XmlIDREF
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Variable> outputs = new HashSet<Variable>();

    @Transient
    private transient RunStrategy runStrategy = new RunStrategy.Post();

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

    public void setInputs(Set<Variable> i) {
        this.inputs.clear();
        if (i != null) {
            inputs.addAll(i);
        }
    }

    public void setOutputs(Set<Variable> o) {
        this.outputs.clear();
        if (o != null) {
            outputs.addAll(o);
        }
    }

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
                params.put(t.getVar().getId() + "", t.getValue_());
            }
        }
        if (!mine.isEmpty()) {
            throw new SimulationException("Missing input variables: " + mine);
        }
        String response = null;
       response = runStrategy.run(url, params);

        result.addAll(U.parseVariableMap(response));
        return result;
    }

    public void setRunStrategy(RunStrategy r) {
        this.runStrategy = r;
    }
}
