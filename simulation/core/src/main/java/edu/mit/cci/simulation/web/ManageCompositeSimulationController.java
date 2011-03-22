package edu.mit.cci.simulation.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.mit.cci.simulation.model.DefaultVariable;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.mit.cci.simulation.model.CompositeSimulation;
import edu.mit.cci.simulation.model.CompositeStepMapping;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.SimulationCreationException;
import edu.mit.cci.simulation.model.Step;
import edu.mit.cci.simulation.model.Variable;
import edu.mit.cci.simulation.web.beans.CompositeSimulationBean;
import edu.mit.cci.simulation.web.beans.MappingBean;
import edu.mit.cci.simulation.web.beans.StepBean;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("/manage/compositeSimulation")
public class ManageCompositeSimulationController {
	private final static Logger _log = Logger.getLogger(ManageCompositeSimulationController.class);
	
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<CompositeSimulation> simulations = CompositeSimulation.findAllCompositeSimulations();
		model.addAttribute("simulations", simulations);
		
		
		return "compositeSimulationList";
		
	}

	@RequestMapping(value = "/edit/{simulationId}", method = RequestMethod.GET)
	public String edit(@PathVariable Long simulationId, Model model) {
		handleEditOperation(model);
		model.addAttribute("simulation", CompositeSimulation.findCompositeSimulation(simulationId));
		
		return "compositeSimulationEdit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String add(Model model) {
		handleEditOperation(model);
		
		return "compositeSimulationEdit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional
	public String add(Model model, HttpServletRequest request, HttpServletResponse response)
	throws SimulationCreationException, IOException {
		CompositeSimulation compSim = new CompositeSimulation();
		String viewName = addUpdate(compSim, model, request, response);
		
		compSim.persist();
		return viewName;
	}
	
	@RequestMapping(value = "/edit/{simulationId}", method = RequestMethod.POST)
	@Transactional
	public String update(@PathVariable Long simulationId, Model model, HttpServletRequest request, 
			HttpServletResponse response) throws SimulationCreationException, IOException {
		CompositeSimulation compSim = CompositeSimulation.findCompositeSimulation(simulationId);
		String viewName = addUpdate(compSim, model, request, response);
		
		compSim.merge();
		return viewName;
	}
	
	
	
	private String addUpdate(CompositeSimulation compSim, Model model, HttpServletRequest request, 
			HttpServletResponse response) throws SimulationCreationException, IOException {
		try {
		
		JSONDeserializer<CompositeSimulationBean> deser = new JSONDeserializer<CompositeSimulationBean>();
		Object o = deser.deserialize(request.getParameter("compositeSimulation"));
		CompositeSimulationBean compSimBean = deser.deserialize(request.getParameter("compositeSimulation"), CompositeSimulationBean.class);
		//CompositeSimulationBean compSimBean = (CompositeSimulationBean) JSONObject.toBean(obj, CompositeSimulationBean.class);
		
	
		/*CompositeSimulation compSim = null;
		if (compSimBean.getId() == null) {
			compSim = new CompositeSimulation();
		}
		else {
			compSim = CompositeSimulation.findCompositeSimulation(compSimBean.getId());
		}*/
		
		// update name
		if (! compSimBean.getName().equals(compSim.getName())) {
			compSim.setName(compSimBean.getName());
		}
		
		// update description
		if (! compSimBean.getDescription().equals(compSim.getDescription())) {
			compSim.setDescription(compSimBean.getDescription());
		}
		
		// update input variables
		Set<Variable> inputs = new HashSet<Variable>();
		for (Long variableId: compSimBean.getInputs()) {
			inputs.add(DefaultVariable.findDefaultVariable(variableId));
		}
		compSim.setInputs(inputs);
		
		// update output variables
		Set<Variable> outputs = new HashSet<Variable>();
		for (Long variableId: compSimBean.getOutputs()) {
			outputs.add(DefaultVariable.findDefaultVariable(variableId));
		}
		compSim.setOutputs(outputs);
		
		int order = 0;
		Map<Long, Step> stepsMap = new HashMap<Long, Step>();
		List<Step> steps = new ArrayList<Step>();
		for (StepBean stepBean: compSimBean.getSteps()) {
			Step step = new Step();
			step.setOrder_(order++);
			
			Set<DefaultSimulation> simulations = new HashSet<DefaultSimulation>();
			for (Long simId: stepBean.getSimulations()) {
				simulations.add(DefaultSimulation.findDefaultSimulation(simId));
			}
			//step.set
			step.setSimulations(simulations);
			steps.add(step);
			
			stepsMap.put(stepBean.getStepId(), step);
		}
		compSim.setSteps(steps);
		
		Set<CompositeStepMapping> mappings = new HashSet<CompositeStepMapping>();
		
		for (MappingBean mappingBean: compSimBean.getMappings()) {
			Step from = stepsMap.get(mappingBean.getFromStep());
			Step to = stepsMap.get(mappingBean.getToStep());
			
			
			CompositeStepMapping mapping = new CompositeStepMapping(compSim, from, to);
			for (Object fromVarIdObj: mappingBean.getMapping().keySet()) {
				Long fromVarId = null;
				if (fromVarIdObj instanceof String) {
					fromVarId = Long.parseLong(fromVarIdObj.toString());
				}
				else if (fromVarIdObj instanceof Integer) {
					fromVarId = ((Integer) fromVarIdObj).longValue();
				}
				else {
					fromVarId = (Long) fromVarIdObj;
				}
				
				Variable fromVar = DefaultVariable.findDefaultVariable(fromVarId);
				Variable toVar = DefaultVariable.findDefaultVariable(mappingBean.getMapping().get(fromVarIdObj));
				
				mapping.addLink(fromVar, toVar);
			}
			mappings.add(mapping);
		}
		compSim.setStepMapping(mappings);
		
		compSim.setSimulationVersion(1L);
		
		compSim.persist();
		
		
		
		
		return "compositeSimulationEdit";
		}
		catch (Throwable e) {
			_log.error("Can't create/update composite simulation", e);
			response.setStatus(400);
			response.getWriter().print(e.getMessage());	
			return null;
		}
	}
	
	private void handleEditOperation(Model model) {
		JSONSerializer serializer = new JSONSerializer();
		List<DefaultSimulation> simulations = DefaultSimulation.findAllDefaultSimulations();
		Map<String, List<Long>> simulationInputs = new HashMap<String, List<Long>>();
		Map<String, List<Long>> simulationOutputs = new HashMap<String, List<Long>>();
		
		for (DefaultSimulation sim: simulations) {
			List<Long> inputs = new ArrayList<Long>();
			List<Long> outputs = new ArrayList<Long>();
			
			simulationInputs.put(String.valueOf(sim.getId()), inputs);
			simulationOutputs.put(String.valueOf(sim.getId()), outputs);
			
			for (Variable var: sim.getInputs()) {
				inputs.add(var.getId());
			}
			
			for (Variable var: sim.getOutputs()) {
				outputs.add(var.getId());
			}
		}
		
		model.addAttribute("simulations", simulations);
		model.addAttribute("simulationsJson", serializer.serialize(simulations));
		
		model.addAttribute("simulationInputs", serializer.include("*").serialize(simulationInputs));
		model.addAttribute("simulationOutputs", serializer.serialize(simulationOutputs));

		List variables = DefaultVariable.findAllDefaultVariables();
		model.addAttribute("variables", variables);
		model.addAttribute("variablesJson", serializer.serialize(variables));

		
	}
	
	
	
	
}
