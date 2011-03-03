package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.Tuple;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "tuples", formBackingObject = Tuple.class)
@RequestMapping("/tuples")
@Controller
public class TupleController {
}
