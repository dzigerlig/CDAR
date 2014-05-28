package cdar.dal.exceptions;

/**
 * The Class UnknownCommentException.
 */
public class UnknownCommentException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unknown comment exception.
	 */
	public UnknownCommentException() {
		super("Unknown Comment");
	}
}
