// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.model.Variable;
import java.lang.String;

privileged aspect ExcelVariable_Roo_JavaBean {
    
    public ExcelSimulation ExcelVariable.getExcelSimulation() {
        return this.excelSimulation;
    }
    
    public void ExcelVariable.setExcelSimulation(ExcelSimulation excelSimulation) {
        this.excelSimulation = excelSimulation;
    }
    
    public String ExcelVariable.getWorksheetName() {
        return this.worksheetName;
    }
    
    public void ExcelVariable.setWorksheetName(String worksheetName) {
        this.worksheetName = worksheetName;
    }
    
    public String ExcelVariable.getCellRange() {
        return this.cellRange;
    }
    
    public void ExcelVariable.setCellRange(String cellRange) {
        this.cellRange = cellRange;
    }
    
    public Variable ExcelVariable.getSimulationVariable() {
        return this.simulationVariable;
    }
    
    public void ExcelVariable.setSimulationVariable(Variable simulationVariable) {
        this.simulationVariable = simulationVariable;
    }
    
}