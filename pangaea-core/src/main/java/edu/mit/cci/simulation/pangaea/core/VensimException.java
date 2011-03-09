package edu.mit.cci.simulation.pangaea.core;

public class VensimException extends Exception {

	private static final long serialVersionUID = 8196350612792996942L;

	public VensimException() {
        super();
    }

    public VensimException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public VensimException(String arg0) {
        super(arg0);
    }

    public VensimException(Throwable arg0) {
        super(arg0);
    }

}
