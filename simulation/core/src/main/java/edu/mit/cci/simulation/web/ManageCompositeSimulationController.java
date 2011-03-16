package edu.mit.cci.simulation.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
	public String add(Model model, HttpServletRequest request) throws SimulationCreationException {
		CompositeSimulation compSim = new CompositeSimulation();
		String viewName = addUpdate(compSim, model, request);
		
		compSim.persist();
		return viewName;
	}
	
	@RequestMapping(value = "/edit/{simulationId}", method = RequestMethod.POST)
	@Transactional
	public String update(@PathVariable Long simulationId, Model model, HttpServletRequest request) throws SimulationCreationException {
		CompositeSimulation compSim = CompositeSimulation.findCompositeSimulation(simulationId);
		String viewName = addUpdate(compSim, model, request);
		
		compSim.merge();
		return viewName;
	}
	
	
	
	private String addUpdate(CompositeSimulation compSim, Model model, HttpServletRequest request) throws SimulationCreationException {
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
			inputs.add(Variable.findVariable(variableId));
		}
		compSim.setInputs(inputs);
		
		// update output variables
		Set<Variable> outputs = new HashSet<Variable>();
		for (Long variableId: compSimBean.getOutputs()) {
			outputs.add(Variable.findVariable(variableId));
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
				
				Variable fromVar = Variable.findVariable(fromVarId);
				Variable toVar = Variable.findVariable(mappingBean.getMapping().get(fromVarIdObj));
				
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
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void handleEditOperation(Model model) {
		JSONSerializer serializer = new JSONSerializer();
		List simulations = DefaultSimulation.findAllDefaultSimulations();
		model.addAttribute("simulations", simulations);
		model.addAttribute("simulationsJson", serializer.serialize(simulations));

		List variables = Variable.findAllVariables();
		model.addAttribute("variables", variables);
		model.addAttribute("variablesJson", serializer.serialize(variables));
		
	}
	
	
	
	
}
