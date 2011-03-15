package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.MappedSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebScaffold(path = "mappedsimulations", formBackingObject = MappedSimulation.class)
@RequestMapping("/mappedsimulations")
@Controller
public class MappedSimulationController {


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/xml")
    @ResponseBody
    public MappedSimulation showXml(@PathVariable("id") Long id, Model model) {
       return MappedSimulation.findMappedSimulation(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/html")
    public String show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("mappedsimulation", MappedSimulation.findMappedSimulation(id));
        model.addAttribute("itemId", id);
        return "mappedsimulations/show";
    }

}
