package edu.mit.cci.simulation.client.model.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class ClientArrayAdapter {

    @XmlElement
    public String data;

    public ClientArrayAdapter() {

    }

    public ClientArrayAdapter(Object[] data) {
        if (data == null) this.data = null;
        else {
            this.data = "[";
            String sep = "";
            for (Object o : data) {
                this.data += (sep + (o == null ? "null" : o.toString()));
                sep = ",";
            }
            this.data += "]";
        }
    }

    private static String[] parseString(String s) {
        if (s == null) {
            return null;
        } else {
            s = s.substring(1, s.length() - 1);
            return s.split(",");
        }
    }

    private static Class[] parseClass(String s) {
        if (s == null) {
            return null;
        } else {            
            String[] st = parseString(s);
            Class[] result = new Class[st.length];
            int i = 0;
            for (String t : st) {
                try {
                    t = t.replaceFirst("class\\s","");
                result[i] = Class.forName(t);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    result[i] = Object.class;
                }
                i++;
            }
            return result;
        }


    }


    public static class StringAdapter extends XmlAdapter<ClientArrayAdapter, String[]> {

        @Override
        public ClientArrayAdapter marshal(String[] v) throws Exception {
            return new ClientArrayAdapter(v);
        }

        @Override
        public String[] unmarshal(ClientArrayAdapter v) throws Exception {
            return parseString(v.data);
        }

    }

    public static class ClassAdapter extends XmlAdapter<ClientArrayAdapter, Class<Object>[]> {

        @Override
        public ClientArrayAdapter marshal(Class<Object>[] v) throws Exception {
            return new ClientArrayAdapter(v);
        }

        @Override
        public Class<Object>[] unmarshal(ClientArrayAdapter v) throws Exception {
            return parseClass(v.data);
        }

    }
}