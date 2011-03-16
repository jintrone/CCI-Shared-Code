package edu.mit.cci.simulation.pangaea.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import edu.mit.cci.simulation.pangaea.core.PangaeaConnection;
import edu.mit.cci.simulation.pangaea.core.PangaeaException;
import edu.mit.cci.simulation.pangaea.core.SimulationResults.ScalarElement;
import edu.mit.cci.simulation.pangaea.core.SimulationInput;
import edu.mit.cci.simulation.pangaea.core.SimulationResults;
import edu.mit.cci.simulation.pangaea.core.Variable;

import org.apache.log4j.Logger;

@Path("/")
@Produces("text/plain")
public class  RootResource {

	public static Logger log = Logger.getLogger(RootResource.class);



	@POST       
	@Produces("text/plain")
	public Response runSimulation(
			@FormParam("Pct change in Developed FF emissions") Double devdchange,
			@FormParam("Pct change in Developing A FF emissions") Double devingchange,
			@FormParam("Pct change in Developing B FF emissions") Double nonchange,
			@FormParam("Global land use emissions change") Double landUseChange,
			@FormParam("Target Sequestration") Double targSequestration,
			@FormParam("Developed start year") Double devdStart,
			@FormParam("Developed target year") Double devdTarget,
			@FormParam("Developing A start year") Double devingAStart,
			@FormParam("Developing A target year") Double devingATarget,
			@FormParam("Developing B start year") Double devingBStart,
			@FormParam("Developing B target year") Double devingBTarget,
			@FormParam("Goal for CO2 in the atmosphere") Double co2inAtm) {

		SimulationInput input = new SimulationInput();
		input.setVariable(SimulationInput.InputVariable.DEVELOPED_FF_CHANGE, devdchange);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGA_FF_CHANGE, devingchange);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGB_FF_CHANGE,nonchange);
		input.setVariable(SimulationInput.InputVariable.DEFORESTATION, landUseChange);
		input.setVariable(SimulationInput.InputVariable.AFFORESTATION,targSequestration);
		input.setVariable(SimulationInput.InputVariable.DEVELOPED_FF_START,devdStart);
		input.setVariable(SimulationInput.InputVariable.DEVELOPED_FF_TARGET,devdTarget);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGA_FF_START,devingAStart);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGA_FF_TARGET,devingATarget);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGB_FF_START,devingBStart);
		input.setVariable(SimulationInput.InputVariable.DEVELOPINGB_FF_TARGET,devingBTarget);
		//input.setVariable(SimulationInput.VensimVariable.AFFORESTATION,targSequestration);
		//input.setVariable(SimulationInput.VensimVariable.C, co2inAtm);
		Response r = null; 
		try {
			PangaeaConnection connection = new PangaeaConnection();
			SimulationResults result = connection.submit(input);
			r = Response.ok(createTextResult(result)).build();
		}
		catch (PangaeaException e) {
			log.error("Exception has been thrown by pangaea core", e);
			r = Response.serverError().build();
		}

		return r;

	}

	String createTextResult(SimulationResults result) {
		StringBuilder buffer = new StringBuilder();
		List<Variable> vars = new ArrayList<Variable>(result.getPopulatedVariables());
		
		Collections.sort(vars, new Comparator<Variable>() {
			public int compare(Variable o1, Variable o2) {
				return o1.getInternalName().compareTo(o2.getInternalName());
			}
		});


        Map<String,Object> tmp = new HashMap<String,Object>();
		for (Variable v: vars) {

            List<ScalarElement> vresult = result.get(v);
            String[] array = new String[vresult.size()];
            for (int i = 0;i<vresult.size();i++) {
                array[i] = vresult.get(i).val.doubleValue()+"";
            }
			tmp.put(v.getInternalName(),array);

		}
		return U.stringify(tmp);
	}

}
