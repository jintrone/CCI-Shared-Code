package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "excelsimulations", formBackingObject = ExcelSimulation.class)
@RequestMapping("/excelsimulations")
@Controller
public class ExcelSimulationController {
}
