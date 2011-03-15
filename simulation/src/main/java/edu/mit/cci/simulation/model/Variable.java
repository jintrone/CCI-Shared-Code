package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * User: jintrone
 * Date: 2/9/11
 * Time: 2:00 PM
 */
@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name="Variable")
@XmlAccessorType(XmlAccessType.NONE)
public class Variable {

    public Variable() {

    }

    public Variable(String name, String description, int arity, int precision, double min, double max) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setPrecision_(precision);
        setMax_(max);
        setMin_(min);
        setDataType(DataType.NUM);
    }

    public Variable(String name, String description, int arity, String[] options) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setOptions(options);
        setDataType(DataType.CAT);
    }

    public Variable(String name, String description, int arity) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setDataType(DataType.TXT);
    }

    @XmlElement(name="Name")
    private String name;

     @XmlElement(name="Description")
    private String description;

    @NotNull
    @XmlElement(name="Arity")
    private Integer arity = 1;


    @Enumerated
    @XmlElement(name="DataType")
    private DataType dataType;

    @XmlElement(name="Precision")
    private Integer precision_;

    @XmlElement(name="Max")
    private Double max_;

    @XmlElement(name="Min")
    private Double min_;


    @XmlElement(name="ExternalName")
    private String externalName;

    private String _optionsRaw;

    @Transient
    @XmlElement(name="Options")
    private String[] options;

    @ManyToOne
    private edu.mit.cci.simulation.model.Variable indexingVariable;

    public String[] getOptions() {
        if (_optionsRaw == null) {
            return null;
        } else if (options == null) {
            options = U.unescape(_optionsRaw, null);
        }
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
        _optionsRaw = U.escape(options, null);
    }

    @XmlAttribute(name="Id")
    @XmlID
    public String getId_() {
        return ""+getId();
    }




}
