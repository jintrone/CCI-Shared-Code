package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.Variable;
import edu.mit.cci.simulation.util.ConcreteSerializableCollection;
import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebScaffold(path = "variables", formBackingObject = Variable.class)
@RequestMapping("/variables")
@Controller
public class VariableController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/xml")
    @ResponseBody
    public Variable showXml(@PathVariable("id") Long id, Model model) {
        return Variable.findVariable(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/html")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("variable", Variable.findVariable(id));
        model.addAttribute("itemId", id);
        return "variables/show";
    }

    @RequestMapping(method = RequestMethod.GET, headers = "accept=text/xml")
    @ResponseBody
    public ConcreteSerializableCollection list(Model model) {
        return U.wrap(Variable.findAllVariables());
    }

    @RequestMapping(method = RequestMethod.GET, headers = "accept=text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("variables", Variable.findVariableEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Variable.countVariables() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("variables", Variable.findAllVariables());
        }
        return "variables/list";
    }


}
