package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.jaxb.JaxbReferenceResolver;

/**
 * User: jintrone
 * Date: 3/19/11
 * Time: 10:28 AM
 */
public class RepositoryManagerFactory implements JaxbReferenceResolver.Factory{
    private static RepositoryManager manager;

    @Override
    public JaxbReferenceResolver instance() {
        return manager;
    }

    public void setInstance(RepositoryManager m) {
        manager = m;
    }
}
