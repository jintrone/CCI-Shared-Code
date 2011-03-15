// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.Step;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect StepController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String StepController.create(@Valid Step step, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("step", step);
            return "steps/create";
        }
        step.persist();
        return "redirect:/steps/" + encodeUrlPathSegment(step.getId().toString(), request);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String StepController.createForm(Model model) {
        model.addAttribute("step", new Step());
        return "steps/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String StepController.show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("step", Step.findStep(id));
        model.addAttribute("itemId", id);
        return "steps/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String StepController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("steps", Step.findStepEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Step.countSteps() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("steps", Step.findAllSteps());
        }
        return "steps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String StepController.update(@Valid Step step, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("step", step);
            return "steps/update";
        }
        step.merge();
        return "redirect:/steps/" + encodeUrlPathSegment(step.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String StepController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("step", Step.findStep(id));
        return "steps/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String StepController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Step.findStep(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/steps?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("defaultsimulations")
    public Collection<DefaultSimulation> StepController.populateDefaultSimulations() {
        return DefaultSimulation.findAllDefaultSimulations();
    }
    
    String StepController.encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
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
