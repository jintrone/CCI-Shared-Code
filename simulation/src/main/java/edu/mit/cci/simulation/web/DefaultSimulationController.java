package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.Variable;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RooWebScaffold(path = "defaultsimulations", formBackingObject = DefaultSimulation.class)
@RequestMapping("/defaultsimulations")
@Controller
public class DefaultSimulationController {

    @ModelAttribute("variables")
    public Collection<Variable> populateVariables() {
        return Variable.findAllVariables();
    }
}
