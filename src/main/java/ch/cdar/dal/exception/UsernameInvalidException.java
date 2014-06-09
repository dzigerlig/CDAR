package ch.cdar.dal.exception;

/**
 * The Class UsernameInvalidException.
 */
public class UsernameInvalidException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new username invalid exception.
	 */
	public UsernameInvalidException() {
		super("This username is invalid");
	}
}
