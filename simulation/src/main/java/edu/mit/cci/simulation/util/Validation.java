package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.model.DataType;
import edu.mit.cci.simulation.model.Tuple;
import edu.mit.cci.simulation.model.Variable;
import org.apache.poi.ss.util.AreaReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: jintrone
 * Date: 3/8/11
 * Time: 8:29 PM
 */
public class Validation {

    public static void validateExcelCoordinates(String cellRange) throws SimulationValidationException {
        AreaReference ref = new AreaReference(cellRange);

        int width = 1 + ref.getLastCell().getCol() - ref.getFirstCell().getCol();
        int height = 1 + ref.getLastCell().getRow() - ref.getLastCell().getRow();

        if (width == 0 || height == 0 || (width > 1 && height > 1)) {
            throw new SimulationValidationException("Cell range must be at least one in both dimensions, but not greater than one in both");
        }
    }

    public static void notNull(Object o, String objectName) throws SimulationValidationException {
        if (o == null) throw new SimulationValidationException(objectName + " cannot be null");
    }

    public static void equalsArity(Variable v, int expected) throws SimulationValidationException {
        if (v.getArity() != expected)
            throw new SimulationValidationException("Variable " + v.getName() + " should have arity " + expected + " but instead has " + v.getArity());
    }

    public static void atMostArity(Variable v, int expected) throws SimulationValidationException {
        if (expected > v.getArity())
            throw new SimulationValidationException("Data is longer than specified arity of "+v.getArity());
    }

    public static void excelUrl(String url) throws SimulationValidationException {
        if (!url.startsWith(ExcelSimulation.EXCEL_URL)) {
            throw new SimulationValidationException("Url for built-in excel models should begin with /excel/");
        }
        //To change body of created methods use File | Settings | File Templates.
    }

    public static void isComplete(Variable v) throws SimulationValidationException {
       if (v == null) {
            throw new SimulationValidationException("Variable is null");
        }
        if (v.getDataType() == null || v.getArity() == null ||
                (v.getDataType()==DataType.NUM && (v.getMin_()==null || v.getMax_()==null)) ||
                (v.getDataType()==DataType.CAT && (v.getOptions()==null || v.getOptions().length==0))) {
            throw new SimulationValidationException("Variable is not completely specified");
        }
    }

    public static void canSet(Tuple tuple, String[] values) throws SimulationValidationException {


        if (values.length>tuple.getVar().getArity() ) {
            throw new SimulationValidationException("Cannot set tuple value with length > expected arity");
        }
    }

    public static void checkDataType(Variable var, String[] v, boolean ignoreNulls) throws SimulationValidationException {

        for (String aV : v) {
            checkDataType(var, aV, ignoreNulls);
        }
    }

    public static void checkDataType(Variable var, String s, boolean ignoreNulls) throws SimulationValidationException {
        List<String> options = var.getDataType()==DataType.CAT? Arrays.asList(var.getOptions()): Collections.<String>emptyList();
        if (ignoreNulls && s == null) return;
            if (var.getDataType() == DataType.NUM) {

                try {
                    Double.parseDouble(s);
                } catch (NumberFormatException ex) {
                    throw new SimulationValidationException("Value  "+s+" cannot be interpreted as a number");
                }
            } else if (var.getDataType() == DataType.CAT  && !options.contains(s)) {
                throw new SimulationValidationException("Value " + s + " is not a known categorical option");
            }
    }
}
