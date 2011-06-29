package edu.mit.cci.simulation.ice.beans;

import com.icesoft.faces.component.inputfile.FileInfo;

import java.io.File;

// This is the file that's uploaded by the client
public class FileToUpload {
	
	// file info attributes
    private FileInfo fileInfo;
    // file that was uplaoded
    private File file;

    /**
     * Create a new InputFileDat object.
     *
     * @param fileInfo FileInfo object created by the inputFile component for
     *                 a given File object.
     */
    public FileToUpload(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        this.file = fileInfo.getFile();
    }

    public FileInfo getFileInfo() {
    	return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
}