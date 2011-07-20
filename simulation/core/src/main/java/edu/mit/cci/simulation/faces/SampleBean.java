package edu.mit.cci.simulation.faces;

import javax.faces.event.ActionEvent;

public class SampleBean {
	private String name;

	public void setName(String name) {
	    this.name = name;
    }

	public String getName() {
	    return name;
    }
	
	
	public void updateName(ActionEvent e) {
		System.out.println("Name: " + name);
		// do nothing...
	}

}
