package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultScenario;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "defaultscenarios", formBackingObject = DefaultScenario.class)
@RequestMapping("/defaultscenarios")
@Controller
public class DefaultScenarioController {
}
