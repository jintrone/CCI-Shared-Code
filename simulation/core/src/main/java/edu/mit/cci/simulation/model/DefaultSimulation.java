package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.excel.server.ExcelRunnerStrategy;
import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.jaxb.JaxbCollection;
import edu.mit.cci.simulation.util.U;
import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
@XmlRootElement(name = "Simulation")
@XmlAccessorType(XmlAccessType.NONE)
public class DefaultSimulation implements Simulation {

    private static Logger log = Logger.getLogger(DefaultSimulation.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @XmlAttribute(name = "Id")
    private Long id;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @XmlElement(name = "Creation")
    private Date created;

    @NotNull
    private Long simulationVersion;

    @XmlElement(name = "Description")
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Url")
    private String url;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "Type")
    @Column(columnDefinition = "LONGTEXT")
    private String type;


    @ManyToMany(cascade = CascadeType.ALL, targetEntity = DefaultVariable.class)
    @XmlJavaTypeAdapter(JaxbCollection.Adapter.class)
    private final Set<Variable> inputs = new HashSet<Variable>();


    @ManyToMany(cascade = CascadeType.ALL, targetEntity = DefaultVariable.class)
    @XmlJavaTypeAdapter(JaxbCollection.Adapter.class)
    private final Set<Variable> outputs = new HashSet<Variable>();

    @Transient
    private transient RunStrategy runStrategy = new RunStrategy.Post();


    public Scenario run(List<Tuple> siminputs) throws SimulationException {



        DefaultScenario result = new DefaultScenario();
        result.setSimulation(this);

        Map<Variable,Tuple> inputMap = new HashMap<Variable, Tuple>();
        for (Tuple t:siminputs) {
            inputMap.put(t.getVar(),t);
        }

        Set<Tuple> response = runRaw(siminputs);
        Set<Variable> outputs = new HashSet<Variable>(getOutputs());
        for (Tuple t : response) {
            if (!outputs.remove(t.getVar())) {
                inputMap.put(t.getVar(),t);
            }
        }
        if (!outputs.isEmpty()) {
            log.warn("Not all outputs were identified, missing: " + outputs);
        }
        result.getValues_().addAll(inputMap.values());
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
    public String getIdAsString() {
        return "" + getId();
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

        List<Tuple> selectedinputs = new ArrayList<Tuple>();
        for (Tuple t : siminputs) {
            if (mine.remove(t.getVar())) {
                selectedinputs.add(t);
            }
        }
        if (!mine.isEmpty()) {
            throw new SimulationException("Missing input variables: " + mine);
        }
        String response = null;
        response = getRunStrategy().run(url, selectedinputs);

        result.addAll(U.parseVariableMap(response, getOutputs()));
        result.addAll(U.parseVariableMap(response,getInputs()));


        return result;
    }

    public void setRunStrategy(RunStrategy r) {
        this.runStrategy = r;
    }

    public RunStrategy getRunStrategy() {
        if (getUrl()!=null && getUrl().startsWith(ExcelSimulation.EXCEL_URL) && !(runStrategy instanceof ExcelRunnerStrategy)) {
            new ExcelRunnerStrategy(this);
        }
        return runStrategy;
    }


    public static Map<String, String> parseTypes(Simulation sim) {
        if (sim.getType() == null) return Collections.emptyMap();
        Map<String, String> result = new HashMap<String, String>();
        for (String type : sim.getType().split(";")) {
            String[] kv = type.split("=");
            if (kv.length > 1) {
                result.put(kv[0], kv[1]);
            }
        }
        return result;
    }

    public DefaultVariable findVariableWithExternalName(String name, boolean input) {
        Set<Variable> vs = input?getInputs():getOutputs();
        for (Variable v:vs) {
            if (name.equals(v.getExternalName())) return (DefaultVariable) v;

        }
        return null;

    }


}
