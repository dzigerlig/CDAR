package ch.cdar.dal.exception;

/**
 * The Class UnknownNodeLinkException.
 */
public class UnknownNodeLinkException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new unknown node link exception.
	 */
	public UnknownNodeLinkException() {
		super("Unknown NodeLink");
	}
}
