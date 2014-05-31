package ch.cdar.dal.exceptions;

/**
 * The Class UnknownEntityException.
 */
public class UnknownEntityException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unknown entity exception.
	 */
	public UnknownEntityException() {
		super("Unknown Entity");
	}
}
