package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultSimulation;
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

@RooWebScaffold(path = "defaultsimulations", formBackingObject = DefaultSimulation.class)
@RequestMapping("/defaultsimulations")
@Controller
public class DefaultSimulationController {


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/xml")
    @ResponseBody
    public DefaultSimulation showXml(@PathVariable("id") Long id, Model model) {
        return DefaultSimulation.findDefaultSimulation(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "accept=text/html")
    public String show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("defaultsimulation", DefaultSimulation.findDefaultSimulation(id));
        model.addAttribute("itemId", id);
        return "defaultsimulations/show";

    }

     @RequestMapping(method = RequestMethod.GET, headers = "accept=text/xml")
    public ConcreteSerializableCollection listXml(Model model) {
        return U.wrap(DefaultSimulation.findAllDefaultSimulations());

    }

    @RequestMapping(method = RequestMethod.GET, headers = "accept=text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("defaultsimulations", DefaultSimulation.findDefaultSimulationEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) DefaultSimulation.countDefaultSimulations() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("defaultsimulations", DefaultSimulation.findAllDefaultSimulations());
        }
        addDateTimeFormatPatterns(model);
        return "defaultsimulations/list";
    }

}
