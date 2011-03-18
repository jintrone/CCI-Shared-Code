package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.*;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 2/14/11
 * Time: 3:11 PM
 */
@Controller
@RequestMapping("/defaultsimulations/{simid}/run")
@SessionAttributes("runsim")
@RooJavaBean
@RooToString
public class RunSimulationForm  {

    private static Logger log = Logger.getLogger(RunSimulationForm.class);

    private Map<String, String> inputs = new HashMap<String, String>();

    private Long simid;

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(@PathVariable("simid") long simId, Model model) {
        DefaultSimulation sim = DefaultSimulation.findDefaultSimulation(simId);
        model.addAttribute("simulation", sim);
        this.simid = sim.getId();
        model.addAttribute("form", this);
        return "defaultsimulations/runsimulation";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(RunSimulationForm form, Model model) throws SimulationException {

        Map<String,Long> remap = new HashMap<String,Long>();
        DefaultSimulation sim = DefaultSimulation.findDefaultSimulation(simid);
        for (Variable v:sim.getInputs()) {
            if (v.getExternalName()!=null) {
                remap.put(v.getExternalName(),v.getId());
            }
        }
        List<Tuple> simInputs = new ArrayList<Tuple>();



        for (Map.Entry<String,String> ent:form.inputs.entrySet()) {
            DefaultVariable v = DefaultVariable.findDefaultVariable(remap.containsKey(ent.getKey()) ? remap.get(ent.getKey()) : Long.parseLong(ent.getKey()));
            if (v == null) throw new SimulationException("Could not identify variable "+ent.getKey());
            Tuple t = new Tuple(v);
            t.setValue_(ent.getValue());
            simInputs.add(t);
        }


        Scenario s = sim.run(simInputs);
        return "redirect:/defaultsimulations";

    }



}
