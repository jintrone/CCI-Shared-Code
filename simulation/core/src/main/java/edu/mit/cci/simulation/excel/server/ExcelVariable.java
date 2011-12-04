package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Variable;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity
public class ExcelVariable {

    @ManyToOne
    private ExcelSimulation excelSimulation;

    private String worksheetName;

    private String cellRange;

    private String rewriteCellRange;

    @ManyToOne
    private DefaultVariable simulationVariable;



    public ExcelVariable() {
    }

    public ExcelVariable(ExcelSimulation sim, DefaultVariable var, String worksheet, String cellrange) {
        super();
        setExcelSimulation(sim);
        setSimulationVariable(var);
        setWorksheetName(worksheet);
        setCellRange(cellrange);

    }

    public ExcelVariable(ExcelSimulation sim, DefaultVariable var, String worksheet, String cellrange, String rewriteCellRange) {
        super();
        setExcelSimulation(sim);
        setSimulationVariable(var);
        setWorksheetName(worksheet);
        setCellRange(cellrange);
        setRewriteCellRange(rewriteCellRange);
    }
}
