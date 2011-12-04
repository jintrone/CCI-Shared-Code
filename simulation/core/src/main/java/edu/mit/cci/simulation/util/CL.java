package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultSimulation;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.util.List;

/**
 * User: jintrone
 * Date: 12/2/11
 * Time: 12:16 PM
 */


public class CL {




    public static void main(String[] args) {

        String[] configs = new String[] {
         "classpath:/META-INF/spring/applicationContext.xml"
        };
        new ClassPathXmlApplicationContext(configs);
        List<DefaultSimulation> existing = DefaultSimulation.findAllDefaultSimulations();
        for (DefaultSimulation sim : existing) {
           System.err.println(sim.getName());
        }
    }
}
