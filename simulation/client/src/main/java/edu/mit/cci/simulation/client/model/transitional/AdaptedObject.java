package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.comm.RepositoryManager;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 4:34 PM
 */
public abstract class AdaptedObject<T> {

    T model;
    private RepositoryManager manager;

    public AdaptedObject(T obj, RepositoryManager manager) {
      this.model = obj;
        this.manager = manager;
    }

    public T model() {
        return model;
    }

    public RepositoryManager manager() {
        return manager;
    }




}
