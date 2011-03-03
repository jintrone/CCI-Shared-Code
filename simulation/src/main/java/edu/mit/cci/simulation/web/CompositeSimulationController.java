package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.CompositeSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "compositesimulations", formBackingObject = CompositeSimulation.class)
@RequestMapping("/compositesimulations")
@Controller
public class CompositeSimulationController {
}
