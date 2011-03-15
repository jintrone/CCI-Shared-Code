package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.CompositeSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebScaffold(path = "compositesimulations", formBackingObject = CompositeSimulation.class)
@RequestMapping("/compositesimulations")
@Controller
public class CompositeSimulationController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/xml")
    @ResponseBody
    public CompositeSimulation showXml(@PathVariable("id") Long id, Model model) {
        return CompositeSimulation.findCompositeSimulation(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/html")
    public String show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("compositesimulation", CompositeSimulation.findCompositeSimulation(id));
        model.addAttribute("itemId", id);
        return "compositesimulations/show";
    }

}
