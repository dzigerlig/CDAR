package ch.cdar.dal.exceptions;

/**
 * The Class WikiCreateUserException.
 */
public class WikiCreateUserException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wiki create user exception.
	 */
	public WikiCreateUserException() {
		super("Wiki User Creation Failed!");
	}
}
