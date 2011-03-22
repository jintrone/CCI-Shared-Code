// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultSimulation;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DefaultSimulationController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String DefaultSimulationController.create(@Valid DefaultSimulation defaultSimulation, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("defaultSimulation", defaultSimulation);
            addDateTimeFormatPatterns(model);
            return "defaultsimulations/create";
        }
        defaultSimulation.persist();
        return "redirect:/defaultsimulations/" + encodeUrlPathSegment(defaultSimulation.getId().toString(), request);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String DefaultSimulationController.createForm(Model model) {
        model.addAttribute("defaultSimulation", new DefaultSimulation());
        addDateTimeFormatPatterns(model);
        return "defaultsimulations/create";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String DefaultSimulationController.update(@Valid DefaultSimulation defaultSimulation, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("defaultSimulation", defaultSimulation);
            addDateTimeFormatPatterns(model);
            return "defaultsimulations/update";
        }
        defaultSimulation.merge();
        return "redirect:/defaultsimulations/" + encodeUrlPathSegment(defaultSimulation.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String DefaultSimulationController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("defaultSimulation", DefaultSimulation.findDefaultSimulation(id));
        addDateTimeFormatPatterns(model);
        return "defaultsimulations/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String DefaultSimulationController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        DefaultSimulation.findDefaultSimulation(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/defaultsimulations?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    void DefaultSimulationController.addDateTimeFormatPatterns(Model model) {
        model.addAttribute("defaultSimulation_created_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
    
    String DefaultSimulationController.encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
