package edu.mit.cci.simulation.client.model.impl;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.model.jaxb.ClientArrayAdapter;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlType(name="DefaultVariable")
public class ClientMetaData implements MetaData {

    private static Logger log = Logger.getLogger(ClientMetaData.class);
    private String description;
    private Long id;
    private String name;
    private Class<Object>[] profile;
    private VarContext varcontext;
    private VarType vartype;
    private String[] units;
    private String[] defaults;
    private String[] maxes;
    private String[] mins;
    private String[] labels;
    private boolean index;
    private String external;
    private String[] categories;
    private MetaData indexingMetaData;



    private String internalname;

    public void setId(Long id) {
        this.id = id;
    }

   
    public ClientMetaData() {

    }



    @XmlElement(name="Description")
    public String getDescription() {
        return description;
    }


    @XmlElement(name="id")
    public Long getId() {
       return id;
    }


    @XmlElement(name="ExternalName")
    public String getInternalName() {
        return internalname;
    }

    @XmlElement(name="name")
    public String getName() {
       return name;
    }

    @XmlElement(name="profile")
    @XmlJavaTypeAdapter(ClientArrayAdapter.ClassAdapter.class)
    public Class<Object>[] getProfile() {
        return profile;
    }

    @XmlAttribute(name="varcontext")
    public VarContext getVarContext() {
        return varcontext;
    }

    @XmlAttribute(name="vartype")
    public VarType getVarType() {
        return vartype;
    }

    @XmlElement(name="units")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getUnits() {
        return units;
    }


    @XmlElement(name="defaults")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getDefault() {
        return defaults;
    }

    @XmlElement(name="maxes")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getMax() {
        return maxes;
    }


    @XmlElement(name="mins")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getMin() {
        return mins;
    }

    @XmlElement(name="labels")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getLabels() {
        return labels;
    }

    @XmlElement(name="external")
    public String getExternalInfo() {
        return external;
    }



    @XmlAttribute(name="index")
    public boolean getIndex() {
        return index;
    }

    @XmlElement(name="categories")
    @XmlJavaTypeAdapter(ClientArrayAdapter.StringAdapter.class)
    public String[] getCategories() {
        return categories;
    }



    public void setIsIndex(boolean b) {
        this.index = b;
    }
    
    @Override
    public void setIndex(boolean b) {
        this.index = b;
    }


    public void setLabels(String[] l) {
        this.labels = l;
    }



    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setInternalName(String name) {
        internalname = name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setProfile(Class<Object>[] profile) {
        this.profile = profile;
    }



    public void setUnits(String[] units) {
        this.units = units;
    }

    public static class Adapter extends XmlAdapter<ClientMetaData,MetaData> {

        @Override
        public ClientMetaData marshal(MetaData v) throws Exception {

            return null;
        }

        @Override
        public MetaData unmarshal(ClientMetaData v) throws Exception {
            return v;
        }

    }

    @Override
    public void setDefault(String[] n) {
        this.defaults = n;

    }



    @Override
    public void setMax(String[] n) {
       this.maxes = n;

    }

    @Override
    public void setMin(String[] n) {
        this.mins = n;

    }


    @Override
    public void setExternalInfo(String info) {
        this.external = info;
    }



    @Override
    public void setCategories(String[] s) {
       this.categories = s;

    }


    @Override
    public void setVarContext(VarContext t) {
        this.varcontext = t;
    }

    @Override
    public void setVarType(VarType t) {
        this.vartype = t;

    }

    @XmlElement(name="indexingmetadata")
    @XmlIDREF
    public MetaData getIndexingMetaData() {
        return indexingMetaData;
    }

    @Override
    public void setIndexingMetaData(MetaData md) {
        indexingMetaData = md;
    }

    public boolean equals(Object o) {
       return (o instanceof MetaData && ((MetaData)o).getId().equals(getId()));
    }

    public int hashCode() {
        return (MetaData.class.hashCode() * getId().hashCode())%13;
    }





}