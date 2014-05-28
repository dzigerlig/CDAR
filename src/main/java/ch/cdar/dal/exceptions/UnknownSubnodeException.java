package ch.cdar.dal.exceptions;

/**
 * The Class UnknownSubnodeException.
 */
public class UnknownSubnodeException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unknown subnode exception.
	 */
	public UnknownSubnodeException() {
		super("Unknown Subnode");
	}
}
