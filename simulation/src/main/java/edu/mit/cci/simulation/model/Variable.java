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
            options = U.unescape(_optionsRaw);
        }
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
        _optionsRaw = U.escape(options);
    }

    @XmlAttribute(name="Id")
    public Long getId_() {
        return getId();
    }




}
