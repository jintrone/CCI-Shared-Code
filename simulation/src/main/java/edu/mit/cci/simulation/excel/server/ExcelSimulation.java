package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.DefaultSimulation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooEntity
public class ExcelSimulation {

    public static final String EXCEL_URL = "/excel/";
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date creation;

    @ManyToOne
    private DefaultSimulation simulation;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ExcelVariable> inputs = new HashSet<ExcelVariable>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ExcelVariable> outputs = new HashSet<ExcelVariable>();

    @Column(columnDefinition = "BLOB")
    private byte[] file;
}
