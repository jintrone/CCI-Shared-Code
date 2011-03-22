// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.Tuple;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

privileged aspect TupleController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String TupleController.create(@Valid Tuple tuple, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("tuple", tuple);
            return "tuples/create";
        }
        tuple.persist();
        return "redirect:/tuples/" + encodeUrlPathSegment(tuple.getId().toString(), request);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String TupleController.createForm(Model model) {
        model.addAttribute("tuple", new Tuple());
        List dependencies = new ArrayList();
        if (DefaultVariable.countDefaultVariables() == 0) {
            dependencies.add(new String[]{"var", "defaultvariables"});
        }
        model.addAttribute("dependencies", dependencies);
        return "tuples/create";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String TupleController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("tuples", Tuple.findTupleEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Tuple.countTuples() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("tuples", Tuple.findAllTuples());
        }
        return "tuples/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String TupleController.update(@Valid Tuple tuple, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("tuple", tuple);
            return "tuples/update";
        }
        tuple.merge();
        return "redirect:/tuples/" + encodeUrlPathSegment(tuple.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String TupleController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("tuple", Tuple.findTuple(id));
        return "tuples/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String TupleController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Tuple.findTuple(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/tuples?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("defaultvariables")
    public Collection<DefaultVariable> TupleController.populateDefaultVariables() {
        return DefaultVariable.findAllDefaultVariables();
    }
    
    String TupleController.encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
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
