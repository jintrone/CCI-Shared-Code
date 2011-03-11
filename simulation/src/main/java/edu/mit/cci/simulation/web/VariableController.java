package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.Variable;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebScaffold(path = "variables", formBackingObject = Variable.class)
@RequestMapping("/variables")
@Controller
public class VariableController {

     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     @ResponseBody
    public Variable show(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("variable", Variable.findVariable(id));
//        model.addAttribute("itemId", id);
//        return "variables/show";
        return Variable.findVariable(id);
    }


}
