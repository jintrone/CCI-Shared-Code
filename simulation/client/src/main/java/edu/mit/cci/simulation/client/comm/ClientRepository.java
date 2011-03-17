package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.EntityState;
import edu.mit.cci.simulation.client.HasId;
import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.model.impl.ClientMetaData;
import edu.mit.cci.simulation.client.model.impl.ClientScenario;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;
import edu.mit.cci.simulation.client.model.jaxb.ResponseWrapper;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: jintrone
 * @date: May 18, 2010
 */
public class ClientRepository {


    private RepositoryManager manager;

    private Connector connector;

    private static Logger log = Logger.getLogger(ClientRepository.class);

    private static ClientRepository instance;

    public static ClientRepository instance(String hostname, int port) throws IOException {
        if (instance == null) {
            instance = new ClientRepository();
            instance.connector = new Connector(hostname, port);
            instance.manager = new RepositoryManager(instance.connector);
            instance.manager.refreshSimulations();
        }
        return instance;
    }

    public static ClientRepository instance() {
        return instance;
    }


    public Collection<Simulation> getAllSimulations() {
        return Collections.unmodifiableCollection(manager.simulationCache.values());
    }

    public Simulation getSimulation(Long id) throws IOException {
        return manager.resolve(String.valueOf(id), ClientSimulation.class);
    }

    public MetaData getMetaData(Long id) throws IOException {
        return manager.resolve(String.valueOf(id), ClientMetaData.class);
    }

    public Scenario getScenario(Long id) throws IOException {
        return manager.resolve(String.valueOf(id), ClientScenario.class);

    }


    public void saveScenario(Scenario s) throws ScenarioNotFoundException, IOException {
        Scenario existing = getScenario(s.getId());
        if (existing == null) {
            throw new ScenarioNotFoundException("Scenario with id " + s.getId() + " could not be found");
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("state", EntityState.PUBLIC.name());
        connector.post(ModelAccessPoint.EDIT_SCENARIO_URL, params, String.valueOf(s.getId()));
    }

    public Scenario runModel(Simulation s, Map<Long, Object> inputs, Long userid, boolean save) throws ModelNotFoundException, IOException, ScenarioNotFoundException {
        Simulation existing = getSimulation(s.getId());
        Scenario result = null;
        if (existing == null) {
            throw new ModelNotFoundException("Simulation with id " + s.getId() + " could not be found");
        }
        Map<String, String> params = new HashMap<String, String>();
        for (MetaData input : s.getInputs()) {
            Object val = inputs.get(input.getId());
            params.put(input.getInternalName()==null?String.valueOf(input.getId()):input.getInternalName(), val == null ? null : val.toString());
        }
        params.put("user", String.valueOf(userid));

        Object o = connector.post(ModelAccessPoint.RUN_MODEL_URL, Collections.<String, String>emptyMap(),String.valueOf(s.getId()));
        if (o instanceof Scenario) return (Scenario)o;
        else {
            log.warn("Error running model");
        }
        return null;
    }

    public Scenario runModelWithInputNames(Simulation s, Map<String, Object> inputs, Long userid, boolean save) throws ModelNotFoundException, IOException, ScenarioNotFoundException, MetaDataNotFoundException {
        Simulation existing = getSimulation(s.getId());
        Scenario result = null;
        if (existing == null) {
            throw new ModelNotFoundException("Simulation with id " + s.getId() + " could not be found");
        }

        Map<String, Long> inputX = new HashMap<String, Long>();
        for (MetaData m : s.getInputs()) {
            if (m.getInternalName()!=null) {
                inputX.put(m.getInternalName(), m.getId());
            }
        }

        Map<Long, Object> ninputs = new HashMap<Long, Object>();
        for (Map.Entry<String, Object> ent : inputs.entrySet()) {
            Long id = inputX.get(ent.getKey());
            if (id == null)
                throw new MetaDataNotFoundException("Metadata with internalname " + ent.getKey() + " not found on simulation " + s.getName());
            ninputs.put(id, ent.getValue());
        }

        return runModel(s, ninputs, userid, save);
    }



}
