package edu.mit.cci.wikipedia.collector;

public class Result {
	private String data;
	private int count;
	private String nextId;
	private boolean archive;
	
	public Result() {
		data = new String();
		count = 0;
		nextId = "0";
		archive = false;
	}
	public void setResult(String _data) {
		data = _data;
	}
	public void append(String _data) {
		data += _data;
		count++;
	}
	public String getResult() {
		return data;
	}
	public int getCount() {
		return count;
	}
	public void clear() {
		data = "";
		count = 0;
		nextId = "0";
		archive = false;
	}
	public void setNextId(String _nextId) {
		nextId = _nextId;
	}
	public String getNextId() {
		return nextId;
	}
	public boolean hasNextId() {
		if (nextId.equals("0"))
			return false;
		else
			return true;
	}
	public boolean getArchive() {
		return archive;
	}
	public void setArchive(boolean flag) {
		archive = flag;
	}
	

}
