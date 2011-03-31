package edu.mit.cci.simulation.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import mit.simulation.climate.model.Simulation;
import mit.simulation.climate.model.persistence.ServerRepository;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import edu.mit.cci.simulation.model.CompositeSimulation;
import edu.mit.cci.simulation.model.DefaultScenario;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Scenario;
import edu.mit.cci.simulation.model.SimulationException;
import edu.mit.cci.simulation.model.Tuple;
import edu.mit.cci.simulation.util.SimulationValidationException;

@Component("migrationTool")
public class MigrationTool {
	private ServerRepository repo;
	private final static String SIMULATION_MAPPING_FILE = "SimulationMapping.txt";
	private final static String VARIABLES_MAPPING_FILE = "VariableMapping.txt";
	private final static String SCENARIO_MAPPING_FILE = "ScenarioMapping.txt";
	private final static String SCENARIO_ERROR_FILE = "ScenarioError.txt";
	
	private final static Logger log = Logger.getLogger(MigrationTool.class);
	
	private Map<Long, Long> simulationMapping = new HashMap<Long, Long>();
	private Map<Long, Long> variableMapping = new HashMap<Long, Long>();
	private Map<Long, Long> scenarioMapping = new HashMap<Long, Long>();
	private Map<Long, String> scenarioErrors = new HashMap<Long, String>();
	
	public MigrationTool() throws MigrationException {
		simulationMapping = loadMappingFile(SIMULATION_MAPPING_FILE);
		variableMapping = loadMappingFile(VARIABLES_MAPPING_FILE);
		
		repo = ServerRepository.instance();
	}
	
	private Map<Long, Long> loadMappingFile(String mappingFileName) throws MigrationException {
		URL mappingFileUrl = MigrationTool.class.getClassLoader().getResource(mappingFileName);
		if (mappingFileUrl == null) {
			throw new MigrationException("Can't find migration file: " + mappingFileName);
		}

		File mappingFile = new File(mappingFileUrl.getFile());
		if (! mappingFile.exists()) {
			throw new MigrationException("Can't find migration file: " + mappingFile.getAbsolutePath());
		}

		Map<Long, Long> mapping = new HashMap<Long, Long>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(mappingFile));
			String line = reader.readLine();

			while (line != null) {
				String[] ids = line.split(",");
				if (ids.length != 2) {
					throw new MigrationException("Invalid format of mapping file, numbers should be splited with comma");
				}

				Long to = Long.parseLong(ids[0]);
				Long from = Long.parseLong(ids[1]);
				mapping.put(from, to);

				line = reader.readLine();
			}
			
			return mapping;

		} catch (FileNotFoundException e) {
			throw new MigrationException("Can't access mapping file", e);
		} catch (IOException e) {
			throw new MigrationException("Can't access mapping file", e);
		} catch (NumberFormatException e) {
			throw new MigrationException("Can't read mapping file (should contain ids separated by commas)", e);
		}


	}
	
	@PersistenceContext 
	private EntityManager em;

	@Transactional
	public void doMigrate() throws MigrationException, IOException {
		// read all scenarios
		int scrCount = repo.getAllScenarios().size();
		int current = 0;
		log.error("Migrating " + scrCount + " scenarios");
		
		Set<Long> scenariosToMigrate = new HashSet<Long>();
		
		scenariosToMigrate.add(7236L);
		scenariosToMigrate.add(7960L);
		scenariosToMigrate.add(7871L);
		scenariosToMigrate.add(7872L);
		
		scenariosToMigrate.add(7726L);
		scenariosToMigrate.add(8735L);
		scenariosToMigrate.add(7800L);
		scenariosToMigrate.add(7783L);
		
		scenariosToMigrate.add(7759L);
		scenariosToMigrate.add(8726L);
		scenariosToMigrate.add(8725L);
		scenariosToMigrate.add(8738L);
		
		scenariosToMigrate.add(8285L);
		scenariosToMigrate.add(8284L);
		scenariosToMigrate.add(8867L);
		
		for (mit.simulation.climate.model.Scenario scenario: repo.getAllScenarios()) {
			/*
			if (! scenariosToMigrate.contains(scenario.getId())) {
				continue;
			}
			*/
			
			int step = (100 * current) / scrCount;
			int prevStep = (100 * (current - 1)) / scrCount;
			
			if (step > prevStep) {
				log.error("Migrated " + (100 * current / scrCount) + "% " + current + " scenarios of " + scrCount);
			}
			
			current ++;
			try {
				if (! simulationMapping.containsKey(scenario.getSimulation().getId())) {
					String msg = "Don't know how to map scenario " + scenario.getId() + ", simulation without mapping: " + scenario.getSimulation().getId();
					log.warn(msg);
					scenarioErrors.put(scenario.getId(), msg);
				}
				else {
					Long simId = simulationMapping.get(scenario.getSimulation().getId());
					
					List<Tuple> inputs = new ArrayList<Tuple>();
					
					for (mit.simulation.climate.model.Variable variable: scenario.getInputSet()) {
						if (! variableMapping.containsKey(variable.getMetaData().getId())) {
							String msg = "Can't find mapping for variable " + variable.getMetaData().getId() + " " + variable.getMetaData().getExternalInfo();
							throw new MigrationException(msg);
						}
						
						DefaultVariable dv = DefaultVariable.findDefaultVariable(variableMapping.get(variable.getMetaData().getId()));
						Tuple t = new Tuple(dv);
						
						t.setValues(variable.getValue().get(0).getValues());
						
						inputs.add(t);
					}

					
					DefaultSimulation sim = DefaultSimulation.findDefaultSimulation(simId);

					DefaultScenario defScenario = (DefaultScenario) sim.run(inputs);
					defScenario.setCreated(new Date());
					defScenario.setName(scenario.getName());
					defScenario.setUser(scenario.getAuthor());
					
					defScenario.persist();
					
					scenarioMapping.put(defScenario.getId(), scenario.getId());
					
					
					//log.info("Should migrate scenario " + scenario.getId());
				}
			}
			catch (org.apache.cayenne.FaultFailureException e) {					
            	String msg = "Cayenne failure for scenario " + scenario.getId() + "\t" + e.getMessage();
            	log.error(msg);
            	scenarioErrors.put(scenario.getId(), msg);
				// ignore
			} catch (SimulationValidationException e) {
				throw new MigrationException(e);
            } catch (SimulationException e) {
				throw new MigrationException(e);
            }
            catch (java.lang.NullPointerException e) {		
            	e.printStackTrace();
            	String msg = "NPE THROWN for scenario " + scenario.getId();
            	log.error(msg);
            	scenarioErrors.put(scenario.getId(), msg);
            }
		}
		
		dumpMapping(scenarioMapping, SCENARIO_MAPPING_FILE);
		dumpMapping(scenarioErrors, SCENARIO_ERROR_FILE);
		
	}
	
	
	private void dumpMapping(Map mapping, String mappingFile) throws IOException {
		File f = new File(mappingFile);
		f.delete();
        PrintWriter writer = new PrintWriter(new FileWriter(f, false));
        for (Object key : mapping.keySet()) {
            writer.println(key + "," + mapping.get(key));
        }
        writer.flush();
        writer.close();
    }

	public static void main(String[] args) throws MigrationException, IOException {
		Locale.setDefault(Locale.US);
		ObjectContext context = DataContext.createDataContext();
        BaseContext.bindThreadObjectContext(DataContext.createDataContext());
		ClassPathXmlApplicationContext springCtx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");

		EntityManagerFactory emf = springCtx.getBean(EntityManagerFactory.class);
		
		EntityManager em = emf.createEntityManager();
		TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
		
		MigrationTool migrationTool = (MigrationTool) springCtx.getBean("migrationTool");//new MigrationTool(SIMULATION_MAPPING_FILE);
		migrationTool.doMigrate();
		
	}

}
