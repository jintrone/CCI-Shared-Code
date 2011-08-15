package edu.mit.cci.simulation.ice.beans;
import javax.faces.event.ActionEvent;
import java.util.Dictionary;

public class Navigation implements java.io.Serializable {

	private static final long serialVersionUID = -6551848046633536366L;
	private int pageNum;
	private String[] pages = {"START", "INITIALIZE_RS", "UPLOAD", "CONFIRM"};
	private String pageType;
	private ResponseSurface responseSurface;
	private String uploadMethod;
	private Boolean[] accessible = {true, false, false, false};
	private boolean popUp;
	
	public Navigation(){
		this.pageNum = 0;
		this.pageType = pages[pageNum];
		
	}
	
	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	public void nextPage(){
		//if(errorMessage(pageType) == null){
			System.out.println("Getting next page, current page is" + pageType);
			this.pageNum ++;
			setPageType(pages[pageNum]);
			if(!accessible[pageNum])
				accessible[pageNum] = true;
			System.out.println("Page Number is: " + pageNum + "   new Page is: " + pageType);
//		}
//		return null;
	}

	public void prevPage(){
		this.pageNum --;
		setPageType(pages[pageNum]);
		System.out.println("Page Number is: " + pageNum + "   new Page is: " + pageType);
	}
	
	public void gotoPage(ActionEvent e){
		
		String newPage = e.getComponent().getId();
		int newPageNum = -1;
		try{
			newPageNum = pageIndexOf(newPage);
		}catch(Exception x){
			System.out.println("Page Does not exist");
		}
		
		if(accessible[newPageNum]){
			pageType = newPage;
			pageNum = newPageNum;
		}
		
	}
	private int pageIndexOf(String page) throws Exception{
		for (int i = 0; i < pages.length; i ++){
			if( page.equals(pages[i]))
				return i;
		}
		throw new Exception("Page does not exist");
	}
	
	public void uploadUsingFile(ActionEvent e){
		this.uploadMethod = "FILE";
	}
	public void uploadByHand(ActionEvent e){
		this.uploadMethod = "HAND";
	}
	public void copyFromExcel(ActionEvent e){
		this.uploadMethod = "COPY";
	}
	
	
//	private String errorMessage(String currentPage){
//		
//		System.out.println("Getting Error Message");
//		String c = currentPage;
//		if(c == "START"){
//			return null;
//		}
//		else if(c == "INITIALIZE_RS"){
//			System.out.println("Currently in INITIALIZE PAGE");
//			System.out.println(responseSurface.getName());
//			if (responseSurface.getName() == null)
//				return "You must enter a name for your Response Surface.";
//			if (responseSurface.getNumScenario() == 0)
//				return "The number of scenarios for your resonse surface cannot be 0.";
//			return null;
//		}
//		return null;
//			
//	}

	
	public String reset(){
		System.out.println("Resetting");
		pageNum = 0;
		setPageType(pages[pageNum]);
		uploadMethod = null;
		for(int i =0; i < accessible.length; i ++){
			accessible[i] = false;
		}
		accessible[0] = true;
		return pages[pageNum];
	}
	
	public void openPopUp(){
		popUp = true;
	}
	public void closePopUp(){
		popUp = false;
	}
	public boolean getPopUp(){
		return popUp;
	}
	
	public ResponseSurface getResponseSurface() {
		return responseSurface;
	}

	public void setResponseSurface(ResponseSurface responseSurface) {
		this.responseSurface = responseSurface;
	}
	
	public String getUploadMethod() {
		return uploadMethod;
	}

	public void setUploadMethod(String uploadMethod) {
		this.uploadMethod = uploadMethod;
	}
	
	public String[] getPages() {
		return pages;
	}

	public void setPages(String[] pages) {
		this.pages = pages;
	}


}
