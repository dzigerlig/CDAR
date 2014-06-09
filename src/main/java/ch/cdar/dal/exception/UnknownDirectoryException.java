package ch.cdar.dal.exception;

/**
 * The Class UnknownDirectoryException.
 */
public class UnknownDirectoryException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unknown directory exception.
	 */
	public UnknownDirectoryException() {
		super("Unknown Directory");
	}
}
