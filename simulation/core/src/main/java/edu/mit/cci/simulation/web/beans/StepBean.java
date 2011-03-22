package edu.mit.cci.simulation.web.beans;

import java.util.ArrayList;
import java.util.List;

public class StepBean {
	private Long id;
	private Long virtId;
	private List<Long> simulations = new ArrayList<Long>();
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public Long getVirtId() {
    	return virtId;
    }
	public void setVirtId(Long virtId) {
    	this.virtId = virtId;
    }
	public List<Long> getSimulations() {
    	return simulations;
    }
	public void setSimulations(List<Long> simulations) {
    	this.simulations = simulations;
    }
	
	public Long getStepId() {
		return id != null ? id : virtId; 
	}

}
