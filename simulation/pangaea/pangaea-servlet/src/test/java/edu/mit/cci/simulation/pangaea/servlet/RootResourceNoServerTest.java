package edu.mit.cci.simulation.pangaea.servlet;

import edu.mit.cci.simulation.pangaea.core.SimulationResults;
import edu.mit.cci.simulation.pangaea.core.Variable;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: jintrone
 * Date: 3/15/11
 * Time: 9:32 PM
 */
public class RootResourceNoServerTest {

    @Test
    public void testStringification() {
        String expect = "BAUCO2Concentration=1.0;2.0;3.0;4.0;5.0&CO2Target=1.0;2.0;3.0;4.0;5.0&GlobalCH4EmissionsCO2e=1.0;2.0;3.0;4.0;5.0&Sea_Level_Rise_output=1.0;3.0;6.0;10.0;15.0&AtmosphericCO2Concentration=1.0;2.0;3.0;4.0;5.0&DevelopingAFossilFuelEmissions=0.27322402596473694;0.5464480519294739;0.8196721076965332;1.0928961038589478;1.3661202192306519&CO2RadiativeForcing=1.0;2.0;3.0;4.0;5.0&RadiativeForcing=1.0;2.0;3.0;4.0;5.0&GlobalTempChange=1.0;2.0;3.0;4.0;5.0&Year=1.0;2.0;3.0;4.0;5.0&DevelopedFossilFuelEmissions=0.27322402596473694;0.5464480519294739;0.8196721076965332;1.0928961038589478;1.3661202192306519&GlobalCO2FFEmissions=1.0;2.0;3.0;4.0;5.0&ExpectedBAUTempChange=1.0;2.0;3.0;4.0;5.0&GlobalN2OEmissionsCO2e=1.0;2.0;3.0;4.0;5.0&DevelopingBFossilFuelEmissions=0.27322402596473694;0.5464480519294739;0.8196721076965332;1.0928961038589478;1.3661202192306519";
        RootResource rr = new RootResource();
        String s = rr.createTextResult(mockSimulationResults());
        Assert.assertEquals(expect, s);

    }

    public SimulationResults mockSimulationResults() {
        SimulationResults results = new SimulationResults();
        for (Variable v: SimulationResults.VensimVariable.values())
        results.addDataPoints(v, new float[]{1f, 2f, 3f, 4f, 5f});
        return results;
    }



}
