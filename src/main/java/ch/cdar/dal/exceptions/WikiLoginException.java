package ch.cdar.dal.exceptions;

/**
 * The Class WikiLoginException.
 */
public class WikiLoginException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wiki login exception.
	 */
	public WikiLoginException() {
		super("Wiki Login Failed");
	}
}
