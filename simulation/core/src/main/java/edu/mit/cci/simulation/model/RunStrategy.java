package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 2/28/11
 * Time: 9:57 AM
 */
public interface RunStrategy {

    public String run(String url, List<Tuple> params) throws SimulationException;


    public static class Post implements RunStrategy {

        @Override
        public String run(String url, List<Tuple> params) throws SimulationException {

            try {
                return U.executePost(url, U.prepareInput(params,true));
            } catch (Exception e) {
                throw new SimulationException(e);
            }
        }
    }




}
