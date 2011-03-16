package edu.mit.cci.simulation.web.beans;

import java.util.ArrayList;
import java.util.List;

public class CompositeSimulationBean {
	private String name;
	private String description;
	private Long id;
	
	private List<Long> inputs = new ArrayList<Long>();
	private List<Long> outputs = new ArrayList<Long>();
	private List<MappingBean> mappings = new ArrayList<MappingBean>();
	private List<StepBean> steps = new ArrayList<StepBean>();
	
	public List<Long> getInputs() {
    	return inputs;
    }
	public void setInputs(List<Long> inputs) {
		for (Object o: inputs) {
			Long val = null;
			if (o instanceof Integer) {
				val = ((Integer) o).longValue();
			}
			else {
				val = (Long) o;
			}
			this.inputs.add(val);
		}
    }
	public List<Long> getOutputs() {
    	return outputs;
    }
	public void setOutputs(List<Long> outputs) {
		for (Object o: outputs) {
			Long val = null;
			if (o instanceof Integer) {
				val = ((Integer) o).longValue();
			}
			else {
				val = (Long) o;
			}
			this.outputs.add(val);
		}
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public List<MappingBean> getMappings() {
    	return mappings;
    }
	public void setMappings(List<MappingBean> mappings) {
    	this.mappings = mappings;
    }
	public List<StepBean> getSteps() {
    	return steps;
    }
	public void setSteps(List<StepBean> steps) {
    	this.steps = steps;
    }
}
