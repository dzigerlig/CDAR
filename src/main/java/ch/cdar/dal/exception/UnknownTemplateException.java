package ch.cdar.dal.exception;

/**
 * The Class UnknownTemplateException.
 */
public class UnknownTemplateException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unknown template exception.
	 */
	public UnknownTemplateException() {
		super("Unknown Template");
	}
}
