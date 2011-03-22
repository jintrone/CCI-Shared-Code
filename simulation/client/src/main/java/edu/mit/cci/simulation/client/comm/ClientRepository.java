package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.HasId;
import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.model.impl.ClientMetaData;
import edu.mit.cci.simulation.client.model.impl.ClientScenario;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;
import edu.mit.cci.simulation.web.RunSimulationForm;
import org.apache.log4j.Logger;

import java.io.IOException;
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

    private DeserializingConnector deserializingConnector;

    private static Logger log = Logger.getLogger(ClientRepository.class);

    private static ClientRepository instance;

    public static ClientRepository instance(String hostname, int port) throws IOException {
        if (instance == null) {
            instance = new ClientRepository();
            instance.deserializingConnector = new BasicConnector(hostname, port);
            instance.manager = new RepositoryManager(instance.deserializingConnector);
            instance.manager.refreshSimulations();
        }
        return instance;
    }

    public static ClientRepository instance(RepositoryManager manager, DeserializingConnector connector) throws IOException {
        if (instance == null) {
            instance = new ClientRepository();
            instance.deserializingConnector = connector;
            instance.manager = manager;
            instance.manager.refreshSimulations();
        }
        return instance;
    }

    public static ClientRepository instance() {
        return instance;
    }

    public RepositoryManager getManager() {
        return manager;
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


    public Set<Simulation> getSimulationsOfType(String type) {
        Set<Simulation> result = new HashSet<Simulation>();
        if (type == null) return Collections.emptySet();
        for (HasId elt : getAllSimulations()) {
            if (elt instanceof Simulation && type.equals(ClientSimulation.parseTypes((Simulation) elt).get("type"))) {
                result.add((Simulation) elt);
            }
        }
        return result;
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
            params.put(input.getId() + "", val == null ? null : val.toString());
        }

        params.put(RunSimulationForm.USER_PARAM,userid+"");

        Object o = manager.getAdaptor(deserializingConnector.post(ModelAccessPoint.RUN_MODEL_URL, params, String.valueOf(s.getId())));
        if (o instanceof Scenario) return (Scenario) o;
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
            if (m.getInternalName() != null) {
                inputX.put(m.getInternalName(), m.getId());
            } else {
                log.info(m.getId() + " is unresolved");
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
