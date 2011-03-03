package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.MappedSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "mappedsimulations", formBackingObject = MappedSimulation.class)
@RequestMapping("/mappedsimulations")
@Controller
public class MappedSimulationController {
}
