package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DefaultScenario;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Variable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/17/11
 * Time: 1:06 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/META-INF/spring/applicationContext.xml","classpath:/webmvc-test-config.xml"})
public class DumpDbToFiles {

    @Test
       @Transactional
       public void dumpData() throws Exception {

           Map<String,String> data = new HashMap<String,String>();

           JAXBContext context = JAXBContext.newInstance(ConcreteSerializableCollection.class, DefaultSimulation.class, DefaultScenario.class);
           Collection<DefaultSimulation> sims = DefaultSimulation.findAllDefaultSimulations();
           ConcreteSerializableCollection wrapper = U.wrap(sims);
           Marshaller m = context.createMarshaller();
           StringWriter writer = new StringWriter();
           m.marshal(wrapper,writer);

           data.put("DefaultSimulations",writer.toString());

           Set<Variable> vars = new HashSet<Variable>();
           for (DefaultSimulation sim:sims) {
               writer = new StringWriter();
               m.marshal(sim,writer);
               data.put("DefaultSimulations."+sim.getId(),writer.toString());
               vars.addAll(sim.getInputs());
               vars.addAll(sim.getOutputs());
           }

           for (Variable v:vars) {
               writer = new StringWriter();
               m.marshal(v,writer);
               data.put("Variables."+v.getId(),writer.toString());

           }

           Collection<DefaultScenario> scenarios = DefaultScenario.findAllDefaultScenarios();
           wrapper = U.wrap(scenarios);
           writer = new StringWriter();
           m.marshal(wrapper,writer);
           data.put("DefaultScenarios",writer.toString());

           for (Map.Entry<String,String> ent:data.entrySet()) {
               FileWriter fwriter = new FileWriter(ent.getKey()+".xml");
               fwriter.write(ent.getValue());
               fwriter.flush();
               fwriter.close();
           }
    }

}
