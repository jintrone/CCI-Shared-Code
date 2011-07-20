package edu.mit.cci.simulation.ice.beans;

import java.util.Date;
import javax.faces.event.ActionEvent;
import edu.mit.cci.simulation.model.DefaultScenario;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.inputfile.FileInfo;
import java.io.*;


public class FileUploader {

	 // latest file uploaded by client
    private FileToUpload file;
    private ResponseSurface responseSurface;
    private String instruction = "Instruction: The first entry of the CSV file must be \"\"; " +
    		"the first row must be populated with the title of X-number of years, in increasing order towards the right;" +
    		"the first column must be populated by Y-number of scenarios, using row 1-Y. The Y+1 row must be empty." +
    		"The first X by Y entries are input values, while the second half of the data are output values. Do not include years for the second half." +
    		"Example: \n" +
    		"2001, 2002, 2003" +
    		"Case1, 0.1, 0.2, 0.3" +
    		"Case2, 0.1, 0.2, 0.3" +
    		"''" +
    		"Case1, 10, 5, 3" +
    		"Case2, 1, 2, 3";
    
	//UploadFile
	public void uploadFile(ActionEvent event) throws Exception {
		
		
        InputFile inputFile = (InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        
        System.err.println(fileInfo.getFileName());
        
        if (fileInfo.getStatus() != FileInfo.INVALID_NAME_PATTERN) {
            file = new FileToUpload(fileInfo);
            System.out.println(file.getFile().getAbsolutePath());
            BufferedReader br = FileReader.read(file.getFile());
            responseSurface.parse(br);
        }
        else{
        	inputFile.getFileInfo().getException().printStackTrace();
        }
	}

	public ResponseSurface getResponseSurface() {
		return responseSurface;
	}

	public void setResponseSurface(ResponseSurface responseSurface) {
		this.responseSurface = responseSurface;
	}

	public String getInstruction() {
		System.err.println("Getting instruction");
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
}

