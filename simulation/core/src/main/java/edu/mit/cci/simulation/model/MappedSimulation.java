package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationComputationException;
import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name = "MappedSimulation")
@XmlAccessorType(XmlAccessType.NONE)
public class MappedSimulation extends DefaultSimulation {

    @ManyToOne
    private DefaultSimulation executorSimulation;

    @ManyToMany
    @JoinTable(name = "VAR_MAPPING")
    private Map<Variable, Variable> variableMap = new HashMap<Variable, Variable>();

    private Integer replication;

    private Integer samplingFrequency = 1;

    @Enumerated
    private ManyToOneMapping manyToOne;

    @ManyToOne
    private Variable indexingVariable;


    public MappedSimulation() {
        setRunStrategy(new RunStrategy() {

            @Override
            public String run(String url, Map<String, String> params) throws SimulationException {
                Map<Variable, Tuple> inputs = U.convertToVarTupleMap(params);
                Map<Variable, String> mergedresults = new HashMap<Variable, String>();
                Set<Tuple> thisrun = new HashSet<Tuple>();
                for (int i = 0; i < replication; i += samplingFrequency) {
                    thisrun.clear();

                    for (Variable input : executorSimulation.getInputs()) {
                        Tuple t = new Tuple(input);
                        Tuple values = inputs.get(getVariableMap().get(input));
                        U.copyRange(values, t, i * input.getArity(), (i + 1) * input.getArity());
                        thisrun.add(t);
                    }

                    Set<Tuple> results = executorSimulation.runRaw(thisrun);

                    for (Tuple t : results) {
                        String s = t.getValue_();
                        if (mergedresults.containsKey(t.getVar())) {
                           s = U.join(mergedresults.get(t.getVar()), s);
                        }
                        mergedresults.put(t.getVar(), s);

                    }


                }

                //remap results
                Map<Variable, Tuple> results = new HashMap<Variable, Tuple>();
                for (Map.Entry<Variable,String> ent:mergedresults.entrySet()) {
                    Tuple t = new Tuple(getVariableMap().get(ent.getKey()));
                    if (manyToOne != null) {
                        try {
                            t.setValues(new String[]{manyToOne.reduce(U.unescape(ent.getValue(), null))});
                        } catch (SimulationComputationException ex) {
                            t.setValues(new String[] {null});
                            t.setStatus(0,TupleStatus.ERR_CALC);
                        }
                    } else {
                        t.setValue_(ent.getValue());
                    }
                     results.put(t.getVar(), t);
                }


                return U.createStringRepresentationFromTuple(results);
            }
        });
    }

    public void setManyToOne(ManyToOneMapping mapping) {
        boolean b = !U.equals(manyToOne, mapping);
        manyToOne = mapping;
        if (b) updateMappings();
    }

    public void setReplication(Integer r) {
        boolean b = !U.equals(replication, r);
        replication = r;
        if (b) updateMappings();
    }

    public void setSamplingFrequency(Integer s) {
        if (s == null || s.intValue() == 0) s = 1;
        boolean b = !U.equals(s, samplingFrequency);
        samplingFrequency = s;
        if (b) updateMappings();
    }

    public void setIndexingVariable(Variable v) {
        this.indexingVariable = v;
        updateIndexVariable();
    }

    public void setExecutorSimulation(DefaultSimulation executorSimulation) {
        this.executorSimulation = executorSimulation;
        updateMappings();
    }

    private void updateIndexVariable() {
        Variable myindex = getVariableMap().get(indexingVariable);
        if (getOutputs() == null || getOutputs().size() == 0) return;
        for (Variable mo : getOutputs()) {
            mo.setIndexingVariable(null);
            if (myindex != null) {
                if (!myindex.equals(mo)) {
                    mo.setIndexingVariable(myindex);
                }
            }
        }

    }

    private void updateMappings() {
        if (executorSimulation == null || replication == null || samplingFrequency == null) return;
        getInputs().clear();
        getOutputs().clear();
        getVariableMap().clear();
        DefaultSimulation esim = getExecutorSimulation();
        for (Variable mappedInput : esim.getInputs()) {
            Variable v = new Variable();
            v.persist();
            getInputs().add(v);
            getVariableMap().put(mappedInput, v);
            U.copy(mappedInput, v);
            v.setArity(replication * mappedInput.getArity());
        }
        int count =  (int) Math.ceil((double) replication / (double) samplingFrequency);
        int outputArity = manyToOne == null ? count : 1;
        for (Variable mo : esim.getOutputs()) {
            Variable v = new Variable();

            getOutputs().add(v);
            getVariableMap().put(mo, v);
            U.copy(mo, v);
            v.setArity(mo.getArity() * outputArity);
            if (getManyToOne() == ManyToOneMapping.SUM) {
                v.setMax_(mo.getMax_()*count);
            }
            v.persist();
        }
        updateIndexVariable();

    }

    public void setVariableMap(Map<Variable, Variable> map) {
        this.variableMap.clear();
        if (map != null) {
            variableMap.putAll(map);
        }
    }
}