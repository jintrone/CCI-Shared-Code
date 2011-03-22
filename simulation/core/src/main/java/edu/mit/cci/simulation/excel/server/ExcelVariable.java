package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.DefaultVariable;
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

    @ManyToOne
    private DefaultVariable simulationVariable;
}
