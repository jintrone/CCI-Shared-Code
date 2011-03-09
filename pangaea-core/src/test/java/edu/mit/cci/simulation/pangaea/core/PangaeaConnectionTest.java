package edu.mit.cci.simulation.pangaea.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.mit.cci.simulation.pangaea.core.SimulationInput.InputVariable;
import edu.mit.cci.testing.TestExperiment;
import edu.mit.cci.testing.TestingUtils;
import edu.mit.cci.testing.TestingUtilsException;

public class PangaeaConnectionTest {
	
	@BeforeClass
	public static void loadProperties() throws TestingUtilsException {
		TestingUtils.loadPropertiesToSystem(PangaeaConnectionTest.class.getClassLoader().getResource("test.properties").getFile());
	}
	
	static SimulationInput[] experimentInputs;
	static TestExperiment[] experiments;
	
	public final static int THREADS_COUNT = 10;
	public final static int STRESS_SIMULATIONS_COUNT = 50000;

	
	@BeforeClass
	public static void readExperiments() throws TestingUtilsException {
		experiments = TestExperiment.parseTestExperiments(PangaeaCoreTestsAll.class.getClassLoader().getResource("testExperiments.xml").getFile());
		experimentInputs = new SimulationInput[experiments.length];
		
		for (int i=0; i < experiments.length; i++) {
			experimentInputs[i] = new SimulationInput();
			for (String inputName: experiments[i].getInputs().keySet()) {
				InputVariable inputVar = InputVariable.valueOf(inputName);
				experimentInputs[i].setVariable(inputVar, Double.parseDouble(experiments[i].getInputs().get(inputName)));
			}
		}
	}
	
	@Test
	public void accuracyTest() throws VensimException, PangaeaException {
		cycleExperiments();
	}
	
	
	@Test
	public void stressMultithread() throws InterruptedException, VensimException {
		runAndWait(new Runnable() {

			public void run() {
				for (int i=0; i < STRESS_SIMULATIONS_COUNT / THREADS_COUNT / experiments.length; i++) {
					
					try {
						cycleExperiments();
					} catch (Throwable e) {
						e.printStackTrace();
						Assert.fail("One of threads failed");
					}

				}
			}
			
		}, THREADS_COUNT);
		
		// check if all open contexts have been released
		Assert.assertEquals("Some vensim contexts have been taken but not released", 
				VensimContextRepository.getInstance().getAvailableContextsCount(), VensimContextRepository.OPEN_CONTEXTS);
	}
	
	private void cycleExperiments() throws VensimException, PangaeaException {
		for (int i = 0; i < experimentInputs.length; i++) {
			PangaeaConnection connection = new PangaeaConnection();
			SimulationResults result = connection.submit(experimentInputs[i]);
			Assert.assertEquals("Pangaea output is different form what it should be for experiment " + 
					experiments[i].getId(), experiments[i].getOutputs().get("toString"), result.toString().trim());
		}
	}
	
	

    private void runAndWait(Runnable runable, int threadsCount) throws InterruptedException {
        Thread[] threads = new Thread[threadsCount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runable);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            if (threads[i].isAlive()) {
                threads[i].join();
            }
        }
    }

}
