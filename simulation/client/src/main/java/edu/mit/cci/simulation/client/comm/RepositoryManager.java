package edu.mit.cci.simulation.client.comm;

import com.sun.xml.internal.bind.IDResolver;
import edu.mit.cci.simulation.client.HasId;
import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import edu.mit.cci.simulation.client.model.impl.ClientMetaData;
import edu.mit.cci.simulation.client.model.impl.ClientScenario;
import edu.mit.cci.simulation.client.model.impl.ClientSimulation;
import edu.mit.cci.simulation.client.model.jaxb.ConcreteSerializableCollection;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * User: jintrone
 * Date: 3/16/11
 * Time: 10:38 AM
 */
public class RepositoryManager implements Deserializer {

    public static String CACHE_PROPERTY = "edu.mit.cci.simulation.client.cachesize";

    private static RepositoryManager instance;
    private Unmarshaller um;
    private Connector connector;
    private static int DEFAULT_SCENARIO_CACHE_SIZE = 100;

    //package scoped for testing
    int currentScenarioCacheSize;

    private static Logger log = Logger.getLogger(RepositoryManager.class);

    LinkedHashMap<String, Scenario> scenarioCache;
    Map<String, Simulation> simulationCache = new HashMap<String, Simulation>();
    Map<String, MetaData> variableCache = new HashMap<String, MetaData>();

    public RepositoryManager(Connector connector) {
        String cachesize = System.getProperty("edu.mit.cci.simulation.client.cachesize");
        this.connector = connector;
        connector.setDeserializer(this);
        currentScenarioCacheSize = cachesize == null ? DEFAULT_SCENARIO_CACHE_SIZE : Integer.parseInt(cachesize);

        //LRU cache
        scenarioCache = new LinkedHashMap<String, Scenario>(currentScenarioCacheSize + 5, .15f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return (scenarioCache.size() > currentScenarioCacheSize);
            }
        };

        try {
            JAXBContext context = JAXBContext.newInstance(Simulation.class, Scenario.class, ConcreteSerializableCollection.class);
            um = context.createUnmarshaller();
            um.setProperty(IDResolver.class.getName(), new IDResolver() {
                @Override
                public void bind(String s, Object o) throws SAXException {
                    register(s, o);
                }

                @Override
                public Callable<HasId> resolve(final String s, final Class aClass) throws SAXException {
                    return new Callable<HasId>() {

                        public HasId call() throws Exception {
                            return deferResolve(s, aClass);
                        }
                    };
                }
            });


        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public void clearCache() {
        simulationCache.clear();
        variableCache.clear();
        scenarioCache.clear();
    }

    public void refreshSimulations() throws IOException {
        //side-effect of this call will register all simulations
        connector.get(ModelAccessPoint.GET_SIMULATION,null);

    }

    public static RepositoryManager instance() {
        return instance;
    }


    public void register(String s, Object o)  {
        if (o instanceof MetaData) {
            variableCache.put(s, (MetaData) o);
        } else if (o instanceof Simulation) {
            simulationCache.put(s, (Simulation) o);
        } else if (o instanceof Scenario) {
            scenarioCache.put(s, (Scenario) o);
        } else {
            log.warn("Tyring to bind reference for type that should not be referenced: " + o.getClass());
        }
    }

    public HasId lookup(String s, Class clazz) {
        HasId result = null;
        if (clazz == ClientSimulation.class) {
            result = simulationCache.get(s);
        } else if (clazz == ClientMetaData.class) {
            result = variableCache.get(s);
        } else if (clazz == ClientScenario.class) {
            result = scenarioCache.get(s);
        }
        return result;
    }

    public <T extends HasId> T retrieve(String ref, Class<T> clazz) throws IOException {
        ModelAccessPoint address = ModelAccessPoint.forClass(clazz);
        return (T) connector.get(clazz, Collections.<String, String>emptyMap(), ref);

    }

    private <T extends HasId> T deferResolve(final String ref, final Class<T> clazz) throws IOException {
        T result = (T) lookup(ref, clazz);
        if (result == null) {
            result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                    new ClientProxyObject<T>(clazz, ref, new ClientProxyObject.Resolver<T>(){
                        public T resolve() throws IOException {
                            return RepositoryManager.this.resolve(ref,clazz);
                        }
                    }));
        }
        return result;
    }

    public <T extends HasId> T resolve(String ref, Class<T> clazz) throws IOException {
        T result = (T) lookup(ref, clazz);
        if (result == null) {
            result = retrieve(ref, clazz);
        }
        return result;
    }



    public Object deserialize(Reader stream) {
        try {

            return um.unmarshal(stream);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
