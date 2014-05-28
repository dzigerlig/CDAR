package ch.cdar.dal.exceptions;

/**
 * The Class UnknownUserException.
 */
public class UnknownUserException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new unknown user exception.
	 */
	public UnknownUserException() {
		super("Unknown user");
	}
}
