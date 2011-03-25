package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationJAXBAdapter;
import edu.mit.cci.simulation.util.VariableJAXBAdapter;
import flexjson.factories.DoubleObjectFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 11:15 PM
 */
@XmlJavaTypeAdapter(VariableJAXBAdapter.class)
public interface Variable {

    public String[] getOptions();
    public void setOptions(String[] options);


    public String getId_();

    public String getName();
    public void setName(String name);

    public Integer getArity();
    public void setArity(Integer arity);

    public Integer getPrecision_();
    public void setPrecision_(Integer precision);

    public DataType getDataType();
    public void setDataType(DataType type);

    public String getDescription();
    public void setDescription(String description);

    public Double getMax_();
    public void setMax_(Double max);

    public Double getMin_();
    public void setMin_(Double min);

    public void setExternalName(String name);
    public String getExternalName();

    public Variable getIndexingVariable();
    public void setIndexingVariable(Variable v);
    
    public String getLabels();
    public void setLabels(String labels);

    public Long getId();



}
