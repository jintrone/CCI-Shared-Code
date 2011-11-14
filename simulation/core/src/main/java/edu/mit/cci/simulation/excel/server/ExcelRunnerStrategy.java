package edu.mit.cci.simulation.excel.server;

import edu.mit.cci.simulation.model.*;
import edu.mit.cci.simulation.util.SimulationComputationException;
import edu.mit.cci.simulation.util.U;
import edu.mit.cci.simulation.util.Validation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/8/11
 * Time: 10:30 AM
 */
public class ExcelRunnerStrategy implements RunStrategy {

    private DefaultSimulation sim;
    private static Logger log = Logger.getLogger(ExcelRunnerStrategy.class);


    public ExcelRunnerStrategy(DefaultSimulation sim) {
        this.sim = sim;
        sim.setRunStrategy(this);
    }

    public String run(String url, List<Tuple> params) throws SimulationException {
        Validation.excelUrl(url);

        Long id = Long.parseLong(url.substring(ExcelSimulation.EXCEL_URL.length()));
        ExcelSimulation esim = ExcelSimulation.findExcelSimulation(id);
        if (esim == null) {
            throw new SimulationException("Could not identify excel model");
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(esim.getFile());
        HSSFWorkbook workbook = null;

        try {
            workbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
        } catch (IOException e) {
            throw new SimulationException("Error loading excel workbook", e);
        }

        if (workbook == null) {
            throw new SimulationException("Workbook could not be found for simulation " + esim.getSimulation().getName());
        }

        Map<Variable,Tuple> vmap = new HashMap<Variable,Tuple>();
        for (Tuple t:params) {
           vmap.put(t.getVar(),t);
        }

        for (ExcelVariable ev : esim.getInputs()) {

            String paramId = ev.getSimulationVariable().getExternalName();
            Tuple input = vmap.get(ev.getSimulationVariable());
            if (input == null) {
              log.warn("Missing input variable: " + ev.getSimulationVariable());
            }
            writeInput(ev, input.getValues(), workbook);
        }

        runForumlas(workbook);
        Map<Variable, Object[]> result = new HashMap<Variable, Object[]>();
        for (ExcelVariable ev : esim.getOutputs()) {
            result.put(ev.getSimulationVariable(), readOutput(ev, workbook));
        }
        return U.createStringRepresentation(result);
    }

    public void writeInput(ExcelVariable v, String[] data, HSSFWorkbook workbook) throws SimulationException {
        HSSFSheet sheet = workbook.getSheet(v.getWorksheetName());
        Validation.equalsArity(v.getSimulationVariable(), data.length);
        Validation.notNull(sheet, "Worksheet");
        Validation.validateExcelCoordinates(v.getCellRange());

        AreaReference area = new AreaReference(v.getCellRange());

        int height = 1+area.getLastCell().getRow() - area.getFirstCell().getRow();
        int width = 1+area.getLastCell().getCol() - area.getFirstCell().getCol();

        int startrow = area.getFirstCell().getRow();
        int startcol = area.getFirstCell().getCol();

        int dx = width == 1 ? 0 : 1;
        int dy = height == 1 ? 0 : 1;

        DataType type = v.getSimulationVariable().getDataType();

        for (int i = 0; i < Math.max(width, height); i++) {
            Cell cell = sheet.getRow(startrow + (dy * i)).getCell(startcol + (i * dx));

            switch (type) {

                case NUM: {
                    Double d = null;
                    if (data[i]==null) {
                        cell.setCellValue(data[i]);
                    } else cell.setCellValue(Double.parseDouble(data[i]));
                    break;
                }

                case TXT: {
                    cell.setCellValue(data[i]);
                    break;
                }

                case CAT: {
                    cell.setCellValue(data[i]);
                    break;
                }
            }
        }


    }

    public void runForumlas(HSSFWorkbook workbook) throws SimulationException {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            for (Row r : sheet) {
                for (Cell c : r) {
                    if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        try {
                            evaluator.evaluateFormulaCell(c);
                        } catch (Exception e) {
                            throw new SimulationException(e);
                        }
                    }
                }
            }

        }
    }

    public Object[] readOutput(ExcelVariable ev, HSSFWorkbook workbook) throws SimulationException {
        HSSFSheet sheet = workbook.getSheet(ev.getWorksheetName());
        Validation.notNull(sheet, "Worksheet");
        Validation.validateExcelCoordinates(ev.getCellRange());

        AreaReference area = new AreaReference(ev.getCellRange());

        int  height= 1 + area.getLastCell().getRow() - area.getFirstCell().getRow();
        int width = 1 + area.getLastCell().getCol() - area.getFirstCell().getCol();

        int startrow = area.getFirstCell().getRow();
        int startcol = area.getFirstCell().getCol();

        int dx = width == 1 ? 0 : 1;
        int dy = height == 1 ? 0 : 1;

        Validation.equalsArity(ev.getSimulationVariable(), Math.max(width, height));
        Object[] result = new Object[ev.getSimulationVariable().getArity()];
        for (int i = 0; i < Math.max(width, height); i++) {
            try {
                result[i] = U.getCellValueAsString(sheet,startrow + (dy * i),startcol + (i * dx),null);
                TupleStatus status = TupleStatus.decode((String)result[i]);
                if (status!=null) {
                    result[i] = status;
                }
            } catch (SimulationComputationException ex) {

                result[i] = TupleStatus.ERR_CALC;
            }
        }
        return result;

    }

}



