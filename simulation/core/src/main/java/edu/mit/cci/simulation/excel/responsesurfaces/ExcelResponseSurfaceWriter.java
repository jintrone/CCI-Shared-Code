package edu.mit.cci.simulation.excel.responsesurfaces;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.*; //Importing HSSFWorkbook, Sheet, Cell, etc.
import org.apache.poi.hssf.util.*;

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
    public static <T extends Comparable<T>,U extends Comparable<U>> String writeSpreadSheet(String filename, int numInputsAndOutputs, SimpleResponseSurface<T,U> responseSurface) {

        //Keren's code 

        HSSFWorkbook workbook = new HSSFWorkbook(); // new HSSFWorkbook(FileInputStream)
        int row = 0;
        //Constants/Variables needed for generating table: [Hardcoded for testing]
        int numSlices = responseSurface.getSlices().size(); //Number of slices: if there are n slices, there will be L1, L2, ... Ln, and Un
        int numIndices = responseSurface.getSlices().get(0).size();
        
        /** CREATE SHEET TWO: DATA **/
        row = 0;
        HSSFSheet dataSheet = workbook.createSheet("Data");
        HSSFRow dataSec1 = dataSheet.createRow(row); row ++;
        dataSec1.createCell(0).setCellValue("SLICE EQUATIONS");
        HSSFRow dataHeader1 = dataSheet.createRow(row); row ++;
        
        //Get Data
        List<Slice<T, U>> slices = responseSurface.getSlices();
        //int numSlices = slices.size(); //Number of slices: if there are n slices, there will be L1, L2, ... Ln, and Un
        
        //Get indecies
        Slice <T, U> sampleSlice = responseSurface.getSlices().get(0);
        List<U> ndx = new ArrayList<U>();
        for (SliceSegment<T,U> seg:sampleSlice) {
          ndx.add(seg.getIndex());
        }
        
        int beginYear = (Integer) ndx.get(0); //Beginning Year of Input
        int dY = (Integer)ndx.get(1)-(Integer)ndx.get(0); //Year interval between Input Years
        //ArrayList<Polynomial> polynomials = new ArrayList<Polynomial>(); 
        
        
        //Writing Header
        dataHeader1.createCell(0).setCellValue("Year");
        for(int i = 1; i <= numSlices; i ++){
        	String m = "m" + i;
        	String b = "b" + i;
        	dataHeader1.createCell(2*i-1).setCellValue(m);
        	dataHeader1.createCell(2*i).setCellValue(b);
        	
        }
        
        //Writing to Cell
        for(int i = 0; i < ndx.size(); i ++){
        	HSSFRow curRow = dataSheet.createRow(row); row ++;
        	curRow.createCell(0).setCellValue(new Double(ndx.get(i).toString()));
        	
        	int col = 1;
        	for(SliceSegment<T,U> seg: responseSurface.getAtIndex(ndx.get(i))){
        		curRow.createCell(col).setCellValue(seg.function.getParam(1)); col ++;
        		curRow.createCell(col).setCellValue(seg.function.getIntercept()); col ++;
        	}
       
        }
       
        row ++; //Empty Row between sections
        dataSheet.createRow(row).createCell(0).setCellValue("SLICE BOUNDARIES"); row ++;//Section TWO starts
        HSSFRow dataHeader2 = dataSheet.createRow(row); row ++;
        
        //Writing Header for section 2
        dataHeader1.createCell(0).setCellValue("Year");
        for(int i = 1; i <= numSlices; i ++){
        	String l = "L" + i;
        	dataHeader2.createCell(i).setCellValue(l);  	
        }
        String u = "U" + numSlices;
        dataHeader2.createCell(numSlices+1).setCellValue(u);
        
        //Writing to Cell in section 2
        for(int i = 0; i < ndx.size(); i ++){
        	HSSFRow curRow = dataSheet.createRow(row); row ++;
        	curRow.createCell(0).setCellValue(new Double(ndx.get(i).toString()));
        	
        	int col = 1;
        	T top = null;
        	for(SliceSegment<T,U> seg: responseSurface.getAtIndex(ndx.get(i))){
        		curRow.createCell(col).setCellValue(new Double(seg.fromCriterion.toString())); col ++;
        		top = seg.toCriterion;
        	}
        	curRow.createCell(col).setCellValue(new Double(top.toString())); col ++;
        }
        
        /** Creating SHEET1: Inputs_Outputs **/
        row = 0;
        HSSFSheet ioSheet = workbook.createSheet("Inputs_Outputs");
        
        //Setup excel Input/Output Header
        HSSFRow header = ioSheet.createRow(row); row ++;
        header.createCell(0).setCellValue("");
        header.createCell(1).setCellValue("Inputs");
        header.createCell(2).setCellValue("");
        header.createCell(3).setCellValue("Outputs");
        HSSFRow header2 = ioSheet.createRow(row); row ++;
        header2.createCell(0).setCellValue("Year");
        header2.createCell(1).setCellValue("Emissions (CO2e)");
        header2.createCell(2).setCellValue("Year");
        header2.createCell(3).setCellValue("%GDP");
        

        //Loop through inputs to generate outputs
        for(int i = 0; i < numInputsAndOutputs; i ++){
        	
        	HSSFRow curRow = ioSheet.createRow(row); row ++;
        	curRow.createCell(0).setCellValue(beginYear + i*dY);
        	curRow.createCell(1).setCellValue("");
        	curRow.createCell(2).setCellValue(beginYear + i*dY);
        	curRow.createCell(3).setCellFormula(getExpression(i+3, numIndices, numSlices));
        }
        
        /**Create new file (Can be done at the end) **/
        String toReturn = "";
        try{
        	File f = new File(filename);
        	System.out.println(f.getAbsolutePath());
        	toReturn = f.getAbsolutePath();
        	System.out.println("File Created...");
        	FileOutputStream fOut = new FileOutputStream(f);
        
			workbook.write(fOut);
			fOut.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return toReturn;
    }
    
    //Return expression needed for output
    private static String getExpression(int i, int numIndices, int numSlices){
    	
    	CellReference ul1 = new CellReference(2, 0);
    	String from1 = ul1.toString();
    	from1 = from1.substring(40, from1.length()-1);
    	
    	CellReference ul2 = new CellReference(numIndices + 5, 0);
    	String from2 = ul2.toString();
    	from2 = from2.substring(40, from2.length()-1);
    	
    	CellReference lr1 = new CellReference(numIndices + 1, 2*numSlices);
    	String to1 = lr1.toString();
    	to1 = to1.substring(40, to1.length()-1);
    	
    	CellReference lr2 = new CellReference(2*numIndices + 4, numSlices+1);
    	String to2 = lr2.toString();
    	to2 = to2.substring(40, to2.length()-1);
    	
    	String boundaries = from2 + ":" + to2;
    	String equations = from1 + ":" + to1;
	    int e = 2;
	    int f = 3;
    	StringBuilder toReturn = new StringBuilder("100*");
    	String end = "";
    	for(int col = 2; col <= numSlices+1;){
    		toReturn.append("IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i);
    		toReturn.append(",Data!"+ boundaries + "," + col + "),");
    		col ++;
    		toReturn.append("Inputs_Outputs!$B" + i + "<VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!"+ boundaries + "," + col +")),");
    		toReturn.append("VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + equations + ","+ e +")*Inputs_Outputs!$B" + i);
    		toReturn.append("+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + equations + "," + f + "),");
    		e +=2;
    		f +=2;
    		end +=")";
    	}
    	int col = numSlices +2;
    	toReturn.append("IF(Inputs_Outputs!$B" + i + ">VLOOKUP(Inputs_Outputs!$A3, Data!" +  boundaries + "," + col + "),0,NA())" + end);
    	
    	
    	/*String toReturn = "100*IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",2),Inputs_Outputs!$B" + i + "<VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",3))," +
    		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",2)*Inputs_Outputs!$B" + i + 
    		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",3),"+
    		"IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",3),Inputs_Outputs!$B" + i + "<VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",4))," +
    		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",4)*Inputs_Outputs!$B" + i + 
    		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",5),"+
    		"IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",4),Inputs_Outputs!$B" + i + "<=VLOOKUP(Inputs_Outputs!$A" + i + 
    		",Data!" + from2 + ":" + to2 + ",5))," +
    		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",6)*Inputs_Outputs!$B" + i + 
    		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from1 + ":" + to1 + ",7)," + 
    		"IF(Inputs_Outputs!$B" + i + ">VLOOKUP(Inputs_Outputs!$A" + i + ",Data!" + from2 + ":" + to2 + ",5),0," +
    		"NA()))))";*/
    	/*String toReturn = "100*IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,2),Inputs_Outputs!$B" + i + "<VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,3))," +
		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,2)*Inputs_Outputs!$B" + i + 
		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,3),"+
		"IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,3),Inputs_Outputs!$B" + i + "<VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,4))," +
		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,4)*Inputs_Outputs!$B" + i + 
		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,5),"+
		"IF(AND(Inputs_Outputs!$B" + i + ">=VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,4),Inputs_Outputs!$B" + i + "<=VLOOKUP(Inputs_Outputs!$A" + i + 
		",Data!$A$15:$E$23,5))," +
		"VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,6)*Inputs_Outputs!$B" + i + 
		"+VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$3:$G$11,7)," + 
		"IF(Inputs_Outputs!$B" + i + ">VLOOKUP(Inputs_Outputs!$A" + i + ",Data!$A$15:$E$23,5),0," +
		"NA()))))";*/
	
    	return toReturn.toString();
    }
    
    public static void main(String[] args){
    	
    	SimpleResponseSurface srs = new SimpleResponseSurface();
    	double[] a1 = {-0.0100, 0.057};
    	SliceSegment a = new SliceSegment(0.122, 0.135, 2023, new Polynomial(a1));
    	SliceSegment b = new SliceSegment(0.081, 0.108, 2035, new Polynomial(a1));
    	SliceSegment c = new SliceSegment(0.075, 0.995, 2047, new Polynomial(a1));
    	Slice s = new Slice();
    	s.add(a);
    	s.add(b);
    	s.add(c);
    	srs.addSlice(s);
    	
    	
    	ExcelResponseSurfaceWriter.writeSpreadSheet("testing.xls", 4, srs);
    }
}
