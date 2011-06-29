package edu.mit.cci.simulation.ice.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.faces.context.FacesContext;
import javax.faces.component.UIInput;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;

import edu.mit.cci.simulation.excel.responsesurfaces.*;



public class ResponseSurface {

	private String name;
	private String description;
	private Integer numScenario;
	private Integer numCol;
	private File file;
	private Integer[] columnHeaders;
	private String[] rowHeaders;
	private DoubleMatrix2D scenarios;
	private DoubleMatrix2D outputs;
	private SimpleResponseSurface<Float, Integer> rs;
	private String rsPath;

	public String reset(ActionEvent e){
		this.name = null;
		this.description =  null;
		this.file = null;
		this.numScenario = null;
		this.columnHeaders = null;
		this.rowHeaders = null;
		this.scenarios = null;
		this.outputs = null;
		this.rs = null;
		this.rsPath = null;
		return null;
		
	}
	
	public void parse(BufferedReader br)throws Exception{
	
		List<String> lines = new ArrayList<String>();
		Integer[] cols = null;
		try{
			System.out.println("Parsing...");
			
			//Figure out number of columns
			String line = br.readLine();
			System.out.println(line);
			String[] strCols = line.split(",");
			cols = new Integer[strCols.length-1];
			for(int i = 1; i < strCols.length; i ++){  		//Start at 1, because 0 is ""
				cols[i-1] = Integer.parseInt(strCols[i]);
			}
			//COLS will be used to create IAMResponseSurfaceEngine
			
			//Make 2D double Matrix, then feed into DoubleMatrix2D
			
			String nextLine;
			while ((nextLine = br.readLine()) != null){
				lines.add(nextLine);
			}
		}catch(IOException e){
			System.exit(0); } 
			String[] strRow = new String[(lines.size()-1)/2];
			double[][] inputMatrix = new double[strRow.length][cols.length];
			double[][] outputMatrix = new double[strRow.length][cols.length];
			
			if(strRow.length != this.numScenario)
				throw new Exception("Num Scenario does not match");
			
			//Loop through everyline
			for(int i = 0; i < lines.size(); i ++){
				String[] strOneRow = lines.get(i).split(",");
				//First half
				if(i < strRow.length){
					strRow[i] = strOneRow[0];
					//Loop through every element in the line
					for(int j = 1; j < strOneRow.length; j++){
						Double newEntry = new Double(0);
						if(!strOneRow[j].isEmpty()){
							newEntry = Double.parseDouble(strOneRow[j]);
						}
						inputMatrix[i][j-1] = newEntry;
						System.out.print(inputMatrix[i][j-1] + " ");
					}	
					System.out.println();
				}
				else if (i > strRow.length){
					//Loop through every element in the line
					for(int j = 1; j < strOneRow.length; j++){
						outputMatrix[i-strRow.length-1][j-1] = Double.parseDouble(strOneRow[j]);
						System.out.print(outputMatrix[i-strRow.length-1][j-1] + " ");
					}
					System.out.println();
				}
			}
			scenarios = new DenseDoubleMatrix2D(inputMatrix);
			outputs = new DenseDoubleMatrix2D(outputMatrix);
			
			//Testing/Display for Cols
			for(int i = 0; i < cols.length; i++){
				System.out.println(cols[i]);
			}
			
			columnHeaders = cols;
			rowHeaders = strRow;

	}

	
	@Autowired
	public void makeRS(ActionEvent e){
		
		System.out.println("Making Response Surface");
		//
		Integer baselineIdx = this.columnHeaders[0];
		//
		IAMResponseSurfaceEngine engine = new IAMResponseSurfaceEngine();
		rs = engine.generateResponseSurface(baselineIdx, columnHeaders, scenarios, outputs);
		
		try{
			String name = this.name + ".xls";
			rsPath = ExcelResponseSurfaceWriter.writeSpreadSheet(name, 4, rs);
		}catch(Throwable t){
			
			t.printStackTrace();
		}
	}
	
	public String getRsPath() {
		return rsPath;
	}

	public void setRsPath(String rsPath) {
		this.rsPath = rsPath;
	}

	//Name
	public String getName() {
		System.out.println("Getting name:" + this.name);
		return name;
	}
	public void setName(String name) {
		System.out.println("Setting Name to " + name);
		this.name = name;
	}
	
	//Description
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	//numScenario
	public Integer getNumScenario() {
		return numScenario;
	}
	public void setNumScenario(Integer numScenario) {
		this.numScenario = numScenario;
	}
	
	//NumCols
	public Integer getNumCol() {
		return numCol;
	}

	public void setNumCol(Integer numCol) {
		this.numCol = numCol;
		this.setColumnHeaders(new Integer[numCol]);
	}

	//file
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	//Column Headers
	public String getColumnHeaders() {
		
		String toReturn =  "";
		try{
			for (int i = 0; i < columnHeaders.length; i ++){
				toReturn += "\t" + columnHeaders[i];
			}
		}catch(NullPointerException e){
			return toReturn;
		}
		return toReturn;
	}
	public void setColumnHeaders(Integer[] columnHeaders) {
		this.columnHeaders = columnHeaders;
	}

	//Inputs/Scenarios
	public String getScenarios() {
		return "Inputs: \n" + DoubleMatrix2DtoString(scenarios);
	}
	public void setScenarios(DoubleMatrix2D scenarios) {
		this.scenarios = scenarios;
	}

	//Outputs
	public String getOutputs() {
		return "Outputs: \n" + DoubleMatrix2DtoString(outputs);
	}
	public void setOutputs(DoubleMatrix2D outputs) {
		this.outputs = outputs;
	}

	//Row Headers
	public String getRowHeaders() {
		String toReturn = "";
		for (int i = 0; i < rowHeaders.length; i ++){
			toReturn += "\t" + rowHeaders[i] + "\t";
		}
		return toReturn;	
	}
	public void setRowHeaders(String[] rowHeaders) {
		this.rowHeaders = rowHeaders;
	}

	private String DoubleMatrix2DtoString(DoubleMatrix2D matrix){
		
		String toReturn = "";
		
		try{
			for(int i =0; i < matrix.rows(); i ++ ){
				toReturn += this.rowHeaders[i] + "\t";
				for(int j = 0; j < matrix.columns(); j ++){
					toReturn += matrix.get(i, j) + "\t";
				}
				toReturn += "\n";
			}
		}catch(NullPointerException e){
			return toReturn;
		}
		return toReturn + "\n";
	}
	
	//Validators:
//	public void validateName(FacesContext context, UIComponent validate, Object value){
//		String name = (String)value;
//		
//		System.err.println("VALIDATENAME: " + name);
//		if(name == null || name == " "){
//			((UIInput)validate).setValid(false);
//			FacesMessage msg = new FacesMessage("You must enter a Name");
//			System.err.println("You must enter a name");
//			context.addMessage(validate.getClientId(context), msg);
//		}
//	}
}
