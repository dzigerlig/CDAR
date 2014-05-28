package cdar.dal.exceptions;

/**
 * The Class CreationException.
 */
public class CreationException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new creation exception.
	 */
	public CreationException() {
		super("Entity has not been created!");
	}
}
