package edu.mit.cci.simulation.excel.responsesurfaces;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * User: jintrone
 * Date: 2/25/11
 * Time: 8:05 AM
 *
 * Takes an existing response surface model and creates an excel
 * spreadsheet that will compute it
 *
 */
public class ExcelResponseSurfaceWriter {


    /**
     * Create an ".xls" spreadsheet based on the input response surface
     *
     * @param filename The name of the file to be produced.
     * @param numInputsAndOutputs The number of input / output rows to generate
     * @param responseSurface The response surface to be serialized
     * @param <T> The datatype of the boundaries of each data set
     * @param <U> The indices within each data set at which the surface is evaluated
     */
    public static <T extends Comparable<T>,U extends Comparable<U>> void writeSpreadSheet(String filename, int numInputsAndOutputs, SimpleResponseSurface<T,U> responseSurface) {

        //TODO implement me! You will need to use one of these:
        HSSFWorkbook workbook = null;
    }
}
