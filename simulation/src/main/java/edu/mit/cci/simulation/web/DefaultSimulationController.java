package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "defaultsimulations", formBackingObject = DefaultSimulation.class)
@RequestMapping("/defaultsimulations")
@Controller
public class DefaultSimulationController {
}
