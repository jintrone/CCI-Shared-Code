package edu.mit.cci.simulation.client.model;

import java.util.Map;

/**
 * @author: jintrone
 * @date: May 22, 2010
 */
public interface Updateable {


    /**
     * Get a map of the elements that have changed and need an update.
     *
     * @return
     */
    public Map<String,String> getUpdate();
    public boolean isDirty();
    
}
