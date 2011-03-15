package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.Tuple;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebScaffold(path = "tuples", formBackingObject = Tuple.class)
@RequestMapping("/tuples")
@Controller
public class TupleController {

     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     @ResponseBody
    public Tuple show(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("tuple", Tuple.findTuple(id));
//        model.addAttribute("itemId", id);
//        return "tuples/show";
        return Tuple.findTuple(id);
    }
}
