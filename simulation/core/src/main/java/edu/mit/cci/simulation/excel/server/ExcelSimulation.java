package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.DefaultSimulation;
import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;



    public ExcelSimulation() {

    }

    public ExcelSimulation(DefaultSimulation sim, File f) throws IOException {
        this.setSimulation(sim);
        setFile(IOUtils.toByteArray(new FileReader(f)));
        setCreation(new Date());
    }

}
