package edu.mit.cci.simulation.ice.beans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.excel.server.ExcelVariable;
import edu.mit.cci.simulation.model.DataType;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;

public class Simulation {

	private String name = "Temp";//TODO: Delete
	private String description = "temp";//TODO: Delete
	private String url;
	private String indepVarName = "Time";
	private String indepVarUnits = "Year";
	private String indepVarLabels = "Year";
	private String indepVarDescription = "Time in Years";
	private String inputVarName = "CO2";//TODO: delete
	private String inputVarUnits = "ppm";//TODO: delete
	private String inputVarLabels = "CO2";//TODO: delete
	private String inputVarDescription = "CO2 Emission";//TODO: delete
	private String outputVarName = "GDP";//TODO: delete
	private String outputVarUnits = "%Change";//TODO: delete
	private String outputVarLabels = "GDP";//TODO: delete
	private String outputVarDescription = "Change in GDP"; //TODO: delete
	private ResponseSurface responseSurface;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIndepVarName() {
		return indepVarName;
	}
	public void setIndepVarName(String indepVarName) {
		this.indepVarName = indepVarName;
	}
	public String getIndepVarUnits() {
		return indepVarUnits;
	}
	public void setIndepVarUnits(String indepVarUnits) {
		this.indepVarUnits = indepVarUnits;
	}
	public String getIndepVarLabels() {
		return indepVarLabels;
	}
	public void setIndepVarLabels(String indepVarLabels) {
		this.indepVarLabels = indepVarLabels;
	}
	public String getIndepVarDescription() {
		return indepVarDescription;
	}
	public void setIndepVarDescription(String indepVarDescription) {
		this.indepVarDescription = indepVarDescription;
	}
	public String getInputVarName() {
		return inputVarName;
	}
	public void setInputVarName(String inputVarName) {
		this.inputVarName = inputVarName;
	}
	public String getInputVarUnits() {
		return inputVarUnits;
	}
	public void setInputVarUnits(String inputVarUnits) {
		this.inputVarUnits = inputVarUnits;
	}
	public String getInputVarLabels() {
		return inputVarLabels;
	}
	public void setInputVarLabels(String inputVarLabels) {
		this.inputVarLabels = inputVarLabels;
	}
	public String getInputVarDescription() {
		return inputVarDescription;
	}
	public void setInputVarDescription(String inputVarDescription) {
		this.inputVarDescription = inputVarDescription;
	}
	public String getOutputVarName() {
		return outputVarName;
	}
	public void setOutputVarName(String outputVarName) {
		this.outputVarName = outputVarName;
	}
	public String getOutputVarUnits() {
		return outputVarUnits;
	}
	public void setOutputVarUnits(String outputVarUnits) {
		this.outputVarUnits = outputVarUnits;
	}
	public String getOutputVarLabels() {
		return outputVarLabels;
	}
	public void setOutputVarLabels(String outputVarLabels) {
		this.outputVarLabels = outputVarLabels;
	}
	public String getOutputVarDescription() {
		return outputVarDescription;
	}
	public void setOutputVarDescription(String outputVarDescrption) {
		this.outputVarDescription = outputVarDescrption;
	}
	public ResponseSurface getResponseSurface() {
		return responseSurface;
	}
	public void setResponseSurface(ResponseSurface responseSurface) {
		this.responseSurface = responseSurface;
	}
	
	
	//Create Simulation
	public void createSimulation(ActionEvent e) throws FileNotFoundException, IOException{
		
		System.out.println("Entering CreateSimulation in Simulation...");//TODO: Delete
		
		//Step1: Create Default Simulation
		DefaultSimulation sim = (DefaultSimulation) createBaseSim();
		
		System.out.println("Base Simulation Created");//TODO: Delete
		
		
		//Step2: Create Excel Simulation
		ExcelSimulation esim = new ExcelSimulation();
		esim.setSimulation(sim);
		
		System.out.println("Excel Simulation Created");//TODO: Delete
		
		//Step3: Set Creation date for Excel Simulation
		esim.setCreation(new Date());
		System.out.println("Date inserted to Excel Simulation");//TODO: Delete
		
		//Step4: Excel Simulation File.
		esim.setFile(IOUtils.toByteArray(new FileInputStream(responseSurface.getRsPath())));
		System.out.println("File Path Set to Excel Simulation. Path: " + responseSurface.getRsPath());//TODO: Delete
		
		System.out.println("Excel IOArity is: " + responseSurface.getExcelIOArity());//TODO: Delete
		//Step5: Add data from Excel to Simulation
		//5.0: Generate Cell Info
		Integer startRow = new Integer(3);
		Integer lastRow = startRow + responseSurface.getExcelIOArity();
		String col1 = "A" + startRow.toString() + ":A" + lastRow.toString();
		String col2 = "B" + startRow.toString() + ":B" + lastRow.toString();
		String col3 = "C" + startRow.toString() + ":C" + lastRow.toString();
		String col4 = "D" + startRow.toString() + ":D" + lastRow.toString();
		
		esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName(indepVarName, true), "Inputs_Outputs", col1));
		esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName(inputVarName, true), "Inputs_Outputs", col2));
		esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName(indepVarName, false), "Inputs_Outputs", col3));
		esim.getInputs().add(new ExcelVariable(esim, sim.findVariableWithExternalName(outputVarName, false), "Inputs_Outputs", col4));
		
		//Step6: Persist Excel Simulation
		esim.persist();
		
		//Step7: Set URL to simulation
		sim.setUrl(ExcelSimulation.EXCEL_URL+esim.getId());
		
		//Step8: Persist Simulation
		sim.persist();
		
		test(sim);//TODO: Delete
	}

	//TODO:DELETE (TESTING)
	private void test(DefaultSimulation sim){
		System.out.println("Simulation URL: " + sim.getUrl());
		System.out.println("Simulation Input Variables: " + sim.getInputs().toString());
		System.out.println("Simulation Output Variables: " + sim.getOutputs().toString());
		
		
	}
	
	private DefaultSimulation createBaseSim() {
		
		//Create Default Simulation
		DefaultSimulation sim = new DefaultSimulation();
		
		//Add Basic info: name, description, creation, url, version, 
		sim.setName(this.name);
		sim.setDescription(this.description);
		sim.setCreated(new Date());
		sim.setUrl(/**/"");
		sim.setSimulationVersion(1l);
		sim.persist();
			//Mapping necessary?
		
		//ADDVARS TODO
		System.out.println("Default Simulation Created...");//TODO: Delete
		
		//Add IndepVar
		System.out.println("adding Variables"); //TODO: Delete
		
		DefaultVariable indepVar = new DefaultVariable(indepVarName,indepVarDescription, responseSurface.getExcelIOArity()); //Max Min?
		DefaultVariable inputDepVar = new DefaultVariable(inputVarName, inputVarDescription, responseSurface.getExcelIOArity());
		DefaultVariable outputDepVar = new DefaultVariable(outputVarName, outputVarDescription, responseSurface.getExcelIOArity());
		
		System.out.println("Adding Variables Part One Finished");//TODO: Delete
		
		indepVar.setUnits(indepVarUnits);
		inputDepVar.setUnits(inputVarUnits);
		outputDepVar.setUnits(outputVarUnits);
		System.out.println("Addinv variable units");//TODO: Delete
		indepVar.setLabels(indepVarLabels);
		inputDepVar.setLabels(inputVarLabels);
		outputDepVar.setLabels(outputVarLabels);
		System.out.println("Adding variable labels");//TODO: Delete
		
		//TODO: indepVar.setDefaultValue();
		//indepVar.setExternalName(indepVarName);
		//inputDepVar.setExternalName(inputVarName);
		//outputDepVar.setExternalName(outputVarName);//external name = internal Name
		indepVar.setDataType(DataType.NUM);
		inputDepVar.setDataType(DataType.NUM);
		outputDepVar.setDataType(DataType.NUM);
		
		System.out.println("Done Editing Variables. Part Two finished. ");//TODO: Delete
		indepVar.persist();
		inputDepVar.persist();
		outputDepVar.persist();
		
		System.out.println("Finished Persisting");//TODO: Delete
		
		sim.getInputs().add(indepVar);
		sim.getInputs().add(inputDepVar);
		sim.getOutputs().add(indepVar);
		sim.getOutputs().add(outputDepVar);
		
		System.out.println("finished adding variable to simulation. ");//TODO: Delete
		
		sim.persist();
		
		return sim;
	}

	
	
	
	
	
	
	
	
}
