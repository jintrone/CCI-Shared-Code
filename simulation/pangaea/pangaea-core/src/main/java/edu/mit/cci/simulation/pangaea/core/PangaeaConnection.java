package edu.mit.cci.simulation.pangaea.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import edu.mit.cci.simulation.pangaea.core.SimulationResults.ScalarElement;


/**
 * @author janusz.
 */
public class PangaeaConnection {

    /**
     * Logger.
     */
    private static Logger log = Logger.getLogger(PangaeaConnection.class);

    /**
     * Name of libname system property from which vensim libname will
     * be taken.
     */
    public static final String DLL_LIBNAME_PARAM = "vensim_lib_name";

    /**
     * Name of vensim model path system property from which vensim model path will
     * be taken.
     */
    public static final String MODEL_PATH_PARAM = "vensim_model_path";

    /**
     * Parameters for initial and final time.
     */
    
    /*
    private static final String INITIAL_TIME_VARNAME = "INITIAL TIME";
    private static final float INITIAL_TIME_VALUE = 2000;

    private static final String FINAL_TIME_VARNAME = "FINAL TIME";
    private static final float FINAL_TIME_VALUE = 2100;

    private static final String EMISSIONS_REFERENCE_YEAR_VARNAME = "Emissions reference year";
    private static final float EMISSIONS_REFERENCE_YEAR_VALUE = 2005;
    
     */

    private static final String APPLY_TO_CO2E="Apply to CO2eq";
    private static final float APPLY_TO_CO2E_VALUE=1;
    /**
     * Helper for Vensim communication.
     */
    private VensimHelper vensimHelper;
    
    private final static int VENSIM_CONTEXT_CREATION_MAX_FAILURE_COUNT = 10;

    /**
     * Initializes vensim.
     * @throws PangaeaException 
     */
    public PangaeaConnection() throws PangaeaException {
        String libName = System.getProperty(DLL_LIBNAME_PARAM);
        String modelPath = System.getProperty(MODEL_PATH_PARAM);
        
        if (libName == null || libName.trim().equals("")) {
        	log.error("Vensim library name has to be set with -D" + DLL_LIBNAME_PARAM);
        	throw new PangaeaException("Vensim library name has to be set with -D" + DLL_LIBNAME_PARAM);
        }
        
        if (modelPath == null || modelPath.trim().equals("")) {
        	log.error("Model path has to be set with -D" + MODEL_PATH_PARAM);
        	throw new PangaeaException("Model path has to be set with -D" + MODEL_PATH_PARAM);
        }
        
		for (int i = 0; i < VENSIM_CONTEXT_CREATION_MAX_FAILURE_COUNT
				&& vensimHelper == null; i++) {
			try {
				log.info("creating new vensim helper\n\tdll lib: " + libName + "\n\tmodel path: " + modelPath);
				vensimHelper = new VensimHelper(libName, modelPath);
			} catch (Throwable e) {
				log.error("An exception was thrown when initializing Vensim, try: " + i, e);
			}
		}
		if (vensimHelper == null) {
			throw new PangaeaException("Can't initialize vensim");
		}
    }


    /**
     * Runs simulation
     *
     * @param input inputs that should be used as model parameters
     * @return simulation results
     * @throws PangaeaException 
     */
    public SimulationResults submit(SimulationInput input) throws PangaeaException {
        SimulationResults result = new SimulationResults();
        try {

	    //   vensimHelper.setVariable(EMISSIONS_REFERENCE_YEAR_VARNAME,EMISSIONS_REFERENCE_YEAR_VALUE);
	    vensimHelper.setVariable(APPLY_TO_CO2E,APPLY_TO_CO2E_VALUE);
	    
            for (SimulationInput.InputVariable var : input.getAllVariables().keySet()) {
                vensimHelper.setVariable(var.internalName, input.getValue(var).floatValue());
            }

            // this doesn't work
            //vensimHelper.setVariable("TIME STEP", 2);

            //vensimHelper.setVariable(INITIAL_TIME_VARNAME, INITIAL_TIME_VALUE);
            //vensimHelper.setVariable(FINAL_TIME_VARNAME, FINAL_TIME_VALUE);

            vensimHelper.run();

            log.debug("Retrieving results");
            for (SimulationResults.VensimVariable vv : SimulationResults.VensimVariable.values()) {
                    float[] val = vensimHelper.getVariable(vv.vensimName);
                    log.debug("Adding data points for " + vv);
                    result.addDataPoints(vv, val);

                    log.debug(printArray(vv.vensimName, val));
                }

            vensimHelper.end();

            for (SimulationResults.CompositeVariable cv: SimulationResults.CompositeVariable.values()) {
                result.addDataPoints(cv,cv.process(result));
		
            }
        } catch (VensimException e) {

            log.error("An exception was thrown when accessing Vensim", e);
            
            // check if context wasn't taken if it was, release it
            if (vensimHelper.getContext() > 0) {
            	try {
	                vensimHelper.end();
                } catch (VensimException e1) {
                	// ignore
                }
            }
            
            throw new PangaeaException("An exception was thrown when accessing Vensim", e);
        }
        return result;

    }


    /**
     * @param args the command line arguments
     * @throws VensimException
     * @throws PangaeaException 
     */
    public static void main(String[] args) throws VensimException, PangaeaException {


        long before = System.currentTimeMillis();
        String libName = System.getProperty(DLL_LIBNAME_PARAM);
        String modelPath = System.getProperty(MODEL_PATH_PARAM);

        if (libName == null) {
            libName = "vendll32";
        }
        if (modelPath == null) {
            modelPath = "/home/janusz/workdir/liferay/vensim/vensim_jni/clearn.vmf";
        }

        System.setProperty(DLL_LIBNAME_PARAM, libName);
        System.setProperty(MODEL_PATH_PARAM, modelPath);

        if (args.length > 0 && args[0].equals("info")) {
            System.out.println(new VensimHelper(libName, modelPath).getVensimInfo());
        } else if (args.length > 0 && args[0].equals("vars")) {
            VensimHelper helper = new VensimHelper(libName, modelPath);
            String[] vars = helper.getVariables();
            for (String var : vars) {
                System.out.println(helper.getVariableInfo(var));
            }
        } else {

            File f = new File(".");
            System.out.println(f.getAbsolutePath());

            PangaeaConnection conn = new PangaeaConnection();

            System.out.println("Retrieving results");
            SimulationResults results = conn.submit(new SimulationInput());
            @SuppressWarnings({ "rawtypes", "unchecked" })
            List<Variable> vars = new ArrayList(Arrays.asList(SimulationResults.VensimVariable.values()));
            vars.addAll(Arrays.asList(SimulationResults.CompositeVariable.values()));

            for (edu.mit.cci.simulation.pangaea.core.Variable v : vars) {
                List<ScalarElement> values = results.get(v);
                System.out.print(v.getInternalName() + ": [");
                if (values != null) {
                    for (ScalarElement val : values) {
                        System.out.print(val + ", ");
                    }
                }
                System.out.println("]");
            }

        }
        System.out.println("Execution time: " + (System.currentTimeMillis() - before));
    }


    /**
     * Returns string representing variable and its values.
     *
     * @param name name of variable that is being printed
     * @param val  array of variable values
     * @return string representing variable and its values
     */
    private static String printArray(String name, float[] val) {
        StringBuilder buf = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(1);
        buf.append(name + ": [");
        for (int i = 0; i < val.length; i++) {
            buf.append(format.format(val[i]));
            if (i < val.length - 1) {
                buf.append(", ");
            }
        }
        buf.append("]\n");
        return buf.toString();
    }
}
