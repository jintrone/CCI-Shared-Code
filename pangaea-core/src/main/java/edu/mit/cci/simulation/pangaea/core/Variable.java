package edu.mit.cci.simulation.pangaea.core;

/**
 * Created by IntelliJ IDEA.
 * User: jintrone
 * Date: Jul 15, 2010
 * Time: 5:14:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Variable {
    String getInternalName();
    float[] modify(float[] input);
}
