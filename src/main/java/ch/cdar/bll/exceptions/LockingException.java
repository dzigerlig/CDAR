package ch.cdar.bll.exceptions;

/**
 * The Class LockingException.
 */
public class LockingException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new locking exception.
	 */
	public LockingException() {
		super("Object locking failed");
	}
}
