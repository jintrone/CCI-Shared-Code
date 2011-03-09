package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.model.Variable;
import org.apache.poi.ss.util.AreaReference;

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

    public static void arity(Variable v, int expected) throws SimulationValidationException {
        if (v.getArity() != expected)
            throw new SimulationValidationException("Variable " + v.getName() + " should have arity " + expected + " but instead has " + v.getArity());
    }

    public static void excelUrl(String url) throws SimulationValidationException {
        if (!url.startsWith(ExcelSimulation.EXCEL_URL)) {
            throw new SimulationValidationException("Url for built-in excel models should begin with /excel/");
        }
        //To change body of created methods use File | Settings | File Templates.
    }
}
