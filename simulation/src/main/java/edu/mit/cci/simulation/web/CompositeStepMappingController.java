package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.CompositeStepMapping;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "compositestepmappings", formBackingObject = CompositeStepMapping.class)
@RequestMapping("/compositestepmappings")
@Controller
public class CompositeStepMappingController {
}
