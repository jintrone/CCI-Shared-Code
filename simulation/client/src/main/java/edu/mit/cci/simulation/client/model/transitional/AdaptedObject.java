package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.comm.RepositoryManager;
import edu.mit.cci.simulation.model.Variable;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 4:34 PM
 */
public abstract class AdaptedObject<T> {

    T proxiedObject;
    private RepositoryManager manager;

    public AdaptedObject(T obj, RepositoryManager manager) {
      this.proxiedObject = obj;
        this.manager = manager;
    }

    public T getProxiedObject() {
        return proxiedObject;
    }

    public RepositoryManager getManager() {
        return manager;
    }




}
