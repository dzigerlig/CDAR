package ch.cdar.dal.exceptions;

/**
 * The Class WrongCredentialsException.
 */
public class WrongCredentialsException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wrong credentials exception.
	 */
	public WrongCredentialsException() {
		super("Wrong user credentials");
	}
}
