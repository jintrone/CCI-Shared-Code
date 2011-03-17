package edu.mit.cci.simulation.client.model.jaxb;

import edu.mit.cci.simulation.client.Tuple;
import edu.mit.cci.simulation.client.TupleStatus;
import edu.mit.cci.simulation.client.model.impl.ClientTuple;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientTupleListAdapter {


    @XmlElement(name = "data")
    String data;

    private static Logger log = Logger.getLogger(ClientTupleListAdapter.class);

    public ClientTupleListAdapter() {

    }

    public ClientTupleListAdapter(List<Tuple> src) {
        if (src == null) data = null;
        else {
            data = "[";
            for (Tuple t : src) {
                String sep = "";
                data += "[";
                for (String n : t.getValues()) {
                    data += (sep + (n == null ? "null" : n.toString()));
                    sep = ",";
                }
                data += "]";
            }
            data += "]";
        }
    }

    public static List<Tuple> parse(String data) {
        String stripped = data.substring(1, data.length() - 1);
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(stripped);
        List<Tuple> result = new ArrayList<Tuple>();
        while (m.find()) {
            String[] splited = m.group(1).contains(",")?m.group(1).split(","):new String[]{m.group(1)};
            ClientTuple tuple = new ClientTuple();
            for (int i = 0; i < splited.length; i++) {
                TupleStatus status = TupleStatus.lookup(splited[i]);
                if (status != null) {
                    log.debug("Setting status of "+status+" on element "+i);
                    tuple.setStatus(i, status);
                    splited[i] = null;
                } else {
                    try {
                        splited[i] = URLDecoder.decode(splited[i], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                              // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            tuple.setValues(splited);
            result.add(tuple);
        }
        return result;
    }


    public static class Adapter extends XmlAdapter<ClientTupleListAdapter, List<Tuple>> {

        @Override
        public ClientTupleListAdapter marshal(List<Tuple> v) throws Exception {
            return new ClientTupleListAdapter(v);
        }

        @Override
        public List<Tuple> unmarshal(ClientTupleListAdapter v) throws Exception {
            return parse(v.data);
        }


    }


}