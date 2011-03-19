package edu.mit.cci.simulation.client.comm;

import java.io.IOException;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 7:24 PM
 */
public interface DeserializingConnector {
    void setDeserializer(Deserializer d);

    Object post(RestAccessPoint location, Map<String, String> postparams, String... pathparam) throws IOException;

    <U> U get(Class<U> clazz, Map<String, String> queryparams, String... pathparam) throws IOException;

    Object get(RestAccessPoint location, Map<String, String> queryparams, String... pathparam) throws IOException;
}
