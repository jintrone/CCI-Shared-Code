// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.CompositeSimulationDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CompositeSimulationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: CompositeSimulationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: CompositeSimulationIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: CompositeSimulationIntegrationTest: @Transactional;
    
    @Autowired
    private CompositeSimulationDataOnDemand CompositeSimulationIntegrationTest.dod;
    
    @Test
    public void CompositeSimulationIntegrationTest.testCountCompositeSimulations() {
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", dod.getRandomCompositeSimulation());
        long count = edu.mit.cci.simulation.model.CompositeSimulation.countCompositeSimulations();
        org.junit.Assert.assertTrue("Counter for 'CompositeSimulation' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testFindCompositeSimulation() {
        edu.mit.cci.simulation.model.CompositeSimulation obj = dod.getRandomCompositeSimulation();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to provide an identifier", id);
        obj = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulation(id);
        org.junit.Assert.assertNotNull("Find method for 'CompositeSimulation' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'CompositeSimulation' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testFindAllCompositeSimulations() {
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", dod.getRandomCompositeSimulation());
        long count = edu.mit.cci.simulation.model.CompositeSimulation.countCompositeSimulations();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'CompositeSimulation', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<edu.mit.cci.simulation.model.CompositeSimulation> result = edu.mit.cci.simulation.model.CompositeSimulation.findAllCompositeSimulations();
        org.junit.Assert.assertNotNull("Find all method for 'CompositeSimulation' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'CompositeSimulation' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testFindCompositeSimulationEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", dod.getRandomCompositeSimulation());
        long count = edu.mit.cci.simulation.model.CompositeSimulation.countCompositeSimulations();
        if (count > 20) count = 20;
        java.util.List<edu.mit.cci.simulation.model.CompositeSimulation> result = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulationEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'CompositeSimulation' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'CompositeSimulation' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testFlush() {
        edu.mit.cci.simulation.model.CompositeSimulation obj = dod.getRandomCompositeSimulation();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to provide an identifier", id);
        obj = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulation(id);
        org.junit.Assert.assertNotNull("Find method for 'CompositeSimulation' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCompositeSimulation(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'CompositeSimulation' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testMerge() {
        edu.mit.cci.simulation.model.CompositeSimulation obj = dod.getRandomCompositeSimulation();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to provide an identifier", id);
        obj = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulation(id);
        boolean modified =  dod.modifyCompositeSimulation(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        edu.mit.cci.simulation.model.CompositeSimulation merged = (edu.mit.cci.simulation.model.CompositeSimulation) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'CompositeSimulation' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", dod.getRandomCompositeSimulation());
        edu.mit.cci.simulation.model.CompositeSimulation obj = dod.getNewTransientCompositeSimulation(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'CompositeSimulation' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'CompositeSimulation' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void CompositeSimulationIntegrationTest.testRemove() {
        edu.mit.cci.simulation.model.CompositeSimulation obj = dod.getRandomCompositeSimulation();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CompositeSimulation' failed to provide an identifier", id);
        obj = edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulation(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'CompositeSimulation' with identifier '" + id + "'", edu.mit.cci.simulation.model.CompositeSimulation.findCompositeSimulation(id));
    }
    
}
