package edu.mit.cci.simulation.client.comm;

import java.io.InputStream;
import java.io.Reader;

/**
 * @author: jintrone
 * @date: May 19, 2010
 */
public interface Deserializer {
    public Object deserialize(Reader stream);
}
