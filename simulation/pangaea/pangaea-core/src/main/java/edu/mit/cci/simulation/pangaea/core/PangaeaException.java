package edu.mit.cci.simulation.pangaea.core;

public class PangaeaException extends Exception {

	private static final long serialVersionUID = -1566461476595685556L;

	public PangaeaException() {
        super();
    }

    public PangaeaException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public PangaeaException(String arg0) {
        super(arg0);
    }

    public PangaeaException(Throwable arg0) {
        super(arg0);
    }

}
