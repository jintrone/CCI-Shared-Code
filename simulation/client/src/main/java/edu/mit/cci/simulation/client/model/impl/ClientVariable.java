package edu.mit.cci.simulation.client.model.impl;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Tuple;
import edu.mit.cci.simulation.client.TupleStatus;
import edu.mit.cci.simulation.client.Variable;

import edu.mit.cci.simulation.client.model.jaxb.ClientTupleListAdapter;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="variable")
public class ClientVariable implements Variable {

    Long id;
    MetaData metadata;
    List<Tuple> values = null;

    public ClientVariable() {

    }

    @XmlAttribute(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name="metadata")
   @XmlIDREF
    public MetaData getMetaData() {
        return metadata;
    }


    @XmlJavaTypeAdapter(ClientTupleListAdapter.Adapter.class)
    @XmlElement(name="values")
    public List<Tuple> getValue() {
        if (values == null) {
            values = new ArrayList<Tuple>();
        }
        return values;
    }
    
//    public String getValueAsJSON() {
//        List<Tuple> value = getValue();
//        String[][] valueForJSON = new String[value.size()][2];
//        int i=0;
//        for (Tuple val: value) {
//            valueForJSON[i++] = val.getValues();
//        }
//        return JSONArray.fromObject(valueForJSON).toString();
//    }

    public void setValue(List<Tuple> t) {
        values = new ArrayList<Tuple>(t);
    }

    @Override
    public void addValue(Tuple t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMetaData(MetaData md) {
        this.metadata = md;

    }


    public static class Adapter extends XmlAdapter<ClientVariable,Variable> {

        @Override
        public ClientVariable marshal(Variable v) throws Exception {
            return null;
        }

        @Override
        public Variable unmarshal(ClientVariable v) throws Exception {
            return v;
        }


    }

     public boolean equals(Object o) {
       return (o instanceof Variable && ((Variable)o).getId().equals(getId()));
    }

    public int hashCode() {
        return (Variable.class.hashCode() * getId().hashCode())%13;
    }

    public boolean hasError(TupleStatus status) {
        for (Tuple t:getValue()) {
            if (t.getAllStatuses().contains(status)) return true;
        }
        return false;
    }



}