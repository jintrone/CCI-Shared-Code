package edu.mit.cci.simulation.web;

import edu.mit.cci.simulation.excel.server.ExcelRunnerStrategy;
import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.excel.server.ExcelVariable;
import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.DefaultVariable;
import edu.mit.cci.simulation.model.DefaultVariable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@RequestMapping("/defaultsimulations/createexcel")
@Controller
@RooJavaBean
@RooToString
public class ExcelSimulationForm {


    private byte[] filedata;

    private List<String> inputRanges = new ArrayList<String>();

    private List<String> outputRanges = new ArrayList<String>();

    private List<String> inputWorksheetNames = new ArrayList<String>();

    private List<String> outputWorksheetNames = new ArrayList<String>();

    private List<DefaultVariable> inputVars = new ArrayList<DefaultVariable>();

    private List<DefaultVariable> outputVars = new ArrayList<DefaultVariable>();

    @Size(max=75)
    private String simulationName;

    @Size(max=1024)
    private String simulationDescription;


    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder)
            throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid ExcelSimulationForm eSim, BindingResult result, Model model, @RequestParam("filedata") MultipartFile fildata, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("excelSimulationForm", new ExcelSimulationForm());
            return "defaultsimulations/createexcel";
        }
        DefaultSimulation sim = createSimulation(eSim);
        return "redirect:/defaultsimulations/" + encodeUrlPathSegment(sim.getId().toString(), request);
    }

    private DefaultSimulation createSimulation(ExcelSimulationForm form) {
      DefaultSimulation sim = new DefaultSimulation();
        sim.getInputs().addAll(form.inputVars);
        sim.getOutputs().addAll(form.outputVars);
        sim.setSimulationVersion(1l);
        sim.setCreated(new Date());
        sim.setName(form.simulationName);
        sim.setDescription(form.simulationDescription);
        sim.persist();


        ExcelSimulation esim = new ExcelSimulation();
        esim.setSimulation(sim);
        esim.persist();
        int i = 0;
        for (DefaultVariable v:form.inputVars) {
            ExcelVariable var = new ExcelVariable();
            var.setWorksheetName(form.inputWorksheetNames.get(i));
            var.setCellRange(form.inputRanges.get(i));
            var.setSimulationVariable(v);
            var.setExcelSimulation(esim);
            var.persist();
            i++;

        }

        i=0;
        for (DefaultVariable v:form.outputVars) {
            ExcelVariable var = new ExcelVariable();
            var.setWorksheetName(form.outputWorksheetNames.get(i));
            var.setCellRange(form.outputRanges.get(i));
            var.setSimulationVariable(v);
            var.setExcelSimulation(esim);
            var.persist();
            i++;
        }
        esim.persist();
        esim.setFile(form.filedata);
        esim.persist();
        sim.setUrl(ExcelSimulation.EXCEL_URL+"/"+esim.getId());
        ExcelRunnerStrategy strategy = new ExcelRunnerStrategy(sim);
        return sim;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("excelSimulationForm", new ExcelSimulationForm());
        return "defaultsimulations/createexcel";
    }

    @ModelAttribute("variables")
    public Collection<DefaultVariable> populateVariables() {
        return DefaultVariable.findAllDefaultVariables();
    }



    String encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
