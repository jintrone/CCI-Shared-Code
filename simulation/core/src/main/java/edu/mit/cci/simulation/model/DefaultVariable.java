package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.jaxb.JaxbReference;
import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * User: jintrone
 * Date: 2/9/11
 * Time: 2:00 PM
 */
@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name = "Variable")
@XmlAccessorType(XmlAccessType.NONE)
public class DefaultVariable implements Variable {

    public DefaultVariable() {

    }

    public DefaultVariable(String name, String description, Integer arity, Integer precision, Double min, Double max) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setPrecision_(precision);
        setMax_(max);
        setMin_(min);
        setDataType(DataType.NUM);
    }

    public DefaultVariable(String name, String description, Integer arity, String[] options) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setOptions(options);
        setDataType(DataType.CAT);
    }

    public DefaultVariable(String name, String description, Integer arity) {
        setName(name);
        setDescription(description);
        setArity(arity);
        setDataType(DataType.TXT);
    }

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Description")
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @NotNull
    @XmlElement(name = "Arity")
    private Integer arity = 1;


    @Enumerated
    @XmlElement(name = "DataType")
    private DataType dataType;

    @XmlElement(name = "Precision")
    private Integer precision_;

    @XmlElement(name = "Max")
    private Double max_;

    @XmlElement(name = "Min")
    private Double min_;


    @XmlElement(name = "ExternalName")
    private String externalName;

    private String _optionsRaw;

    @Transient
    @XmlElement(name = "Options")
    private String[] options;

    @ManyToOne(targetEntity = DefaultVariable.class)
    @XmlJavaTypeAdapter(JaxbReference.Adapter.class)
    private Variable indexingVariable;

    @Override
    public String[] getOptions() {
        if (_optionsRaw == null) {
            return null;
        } else if (options == null) {
            options = U.unescape(_optionsRaw, null, null);
        }
        return options;
    }

    @Override
    public void setOptions(String[] options) {
        this.options = options;
        _optionsRaw = U.escape(options, null);
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @XmlElement(name = "Units")
    private String units;

    @Override
    public String getLabels() {
        return labels;
    }

    @Override
    public void setLabels(String labels) {
        this.labels = labels;
    }

    @XmlElement
    private String labels;

    @XmlElement(name = "Defaults")
    private String defaultValue;

    @Override
    public String getId_() {
        return "" + getId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @XmlAttribute(name = "Id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
