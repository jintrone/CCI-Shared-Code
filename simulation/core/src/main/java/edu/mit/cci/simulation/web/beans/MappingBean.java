package edu.mit.cci.simulation.web.beans;

import java.util.HashMap;
import java.util.Map;

public class MappingBean {
	
	private Map<String, Long> mapping = new HashMap<String, Long>();
	private Long fromStep;
	private Long toStep;
	private Long virtId;
	private Long id;
	
	public Map<String, Long> getMapping() {
    	return mapping;
    }
	public void setMapping(Map<String, Long> mapping) {
    	this.mapping = mapping;
    }
	public Long getFromStep() {
    	return fromStep;
    }
	public void setFromStep(Long fromStep) {
    	this.fromStep = fromStep;
    }
	public Long getToStep() {
    	return toStep;
    }
	public void setToStep(Long toStep) {
    	this.toStep = toStep;
    }
	public Long getVirtId() {
    	return virtId;
    }
	public void setVirtId(Long virtId) {
    	this.virtId = virtId;
    }
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }

}
