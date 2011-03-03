package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.Variable;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "variables", formBackingObject = Variable.class)
@RequestMapping("/variables")
@Controller
public class VariableController {
}
