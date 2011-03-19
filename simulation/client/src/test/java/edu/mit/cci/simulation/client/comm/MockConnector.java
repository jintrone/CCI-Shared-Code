package edu.mit.cci.simulation.client.comm;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import edu.mit.cci.simulation.model.DefaultSimulation;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 7:21 PM
 */
public class MockConnector implements DeserializingConnector {


    Deserializer d = null;

    Map<String,String> responses;

    public void setMockResponses(Map<String,String> map) {
      responses = map;
    }

    public MockConnector(Map<String,String> map) {
        setMockResponses(new HashMap<String,String>(map));
    }

    @Override
    public void setDeserializer(Deserializer d) {
       this.d = d;
    }

    @Override
    public Object post(RestAccessPoint location, Map<String, String> postparams, String... pathparam) throws IOException {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public <U> U get(Class<U> clazz, Map<String, String> queryparams, String... pathparam) throws IOException {
        return (U)d.deserialize(new StringReader(responses.get(key(clazz, pathparam!=null&&pathparam.length>0?pathparam[0]:null))));
    }

    @Override
    public Object get(RestAccessPoint location, Map<String, String> queryparams, String... pathparam) throws IOException {
       throw new RuntimeException("Not implemented!");
    }

    public String key(Class clz, String id) {
      return clz.getSimpleName()+"s"+(id!=null?"."+id:"");
    }
}
