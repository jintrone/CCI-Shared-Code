package edu.mit.cci.testing;

/**
 * Exception informing about an error in testing utils. 
 * 
 * @author Janusz Parfieniuk
 *
 */
public class TestingUtilsException extends Exception {

    private static final long serialVersionUID = 1L;

	public TestingUtilsException() {
	    super();
    }

	public TestingUtilsException(String arg0, Throwable arg1) {
	    super(arg0, arg1);
    }

	public TestingUtilsException(String arg0) {
	    super(arg0);
    }

	public TestingUtilsException(Throwable arg0) {
	    super(arg0);
    }

}
