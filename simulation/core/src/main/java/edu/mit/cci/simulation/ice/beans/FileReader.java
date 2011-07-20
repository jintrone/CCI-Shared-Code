package edu.mit.cci.simulation.ice.beans;
import java.io.*;
import java.util.*;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class FileReader {

	public static BufferedReader read(File f){
		
		try{
			System.out.print("Reading... \n");
			FileInputStream fstream = new FileInputStream(f);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			return br;
//			String strLine;
//			//Read File Line By Line
//			while ((strLine = br.readLine()) != null)   {
//				// Print the content on the console
//				System.out.println(strLine + "\n");
//			}
			//Close the input stream
			//in.close();
		}catch(IOException e){
			System.err.print("FILE NOT FOUND");
		}
		return null;
	
	}
	
	//Returns the necessary information to construct response surface
	//List of Column headers, array for scenarios and outputs
	//TODO: what about baseLineIdx?
	public static Object[] parse(BufferedReader br){
		
		try{
			System.out.println("Parsing...");
			
			//Figure out number of columns
			String line = br.readLine();
			String[] strCols = line.split(",");
			Integer[] cols = new Integer[strCols.length-1];
			for(int i = 1; i < strCols.length; i ++){  		//Start at 1, because 0 is ""
				cols[i-1] = Integer.parseInt(strCols[i]);
			}
			//COLS will be used to create IAMResponseSurfaceEngine
			
			//Make 2D double Matrix, then feed into DoubleMatrix2D
			List<String> lines = new ArrayList<String>();
			String nextLine;
			while ((nextLine = br.readLine()) != null){
				lines.add(nextLine);
			}
			String[] strRow = new String[(lines.size()-1)/2];
			double[][] inputMatrix = new double[strRow.length][cols.length];
			double[][] outputMatrix = new double[strRow.length][cols.length];
			//Loop through everyline
			for(int i = 0; i < lines.size(); i ++){
				String[] strOneRow = lines.get(i).split(",");
				//First half
				if(i < strRow.length){
					strRow[i] = strOneRow[0];
					//Loop through every element in the line
					for(int j = 1; j < strOneRow.length; j++){
						inputMatrix[i][j-1] = Double.parseDouble(strOneRow[j]);
						System.out.print(inputMatrix[i][j-1] + " ");
					}	
					System.out.println();
				}
				else if (i > strRow.length){
					//Loop through every element in the line
					for(int j = 1; j < strOneRow.length; j++){
						outputMatrix[i-strRow.length-1][j-1] = Double.parseDouble(strOneRow[j]);
						System.out.print(outputMatrix[i-strRow.length-1][j-1] + " ");
					}
					System.out.println();
				}
			}
			DoubleMatrix2D scenarios = new DenseDoubleMatrix2D(inputMatrix);
			DoubleMatrix2D outputs = new DenseDoubleMatrix2D(outputMatrix);
			
			//Testing/Display for Cols
			for(int i = 0; i < cols.length; i++){
				System.out.println(cols[i]);
			}
			
			Object[] toReturn = {cols, scenarios, outputs};
			return toReturn;
			
			
		}catch(IOException e){
			System.exit(0);
		}
		return null;
		
	}
	public static void main(String[] args){
		
		File f = new File("TestFile1.txt");
		FileReader.read(f);
		
	}
}
