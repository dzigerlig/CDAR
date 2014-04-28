package cdar.dal.exceptions;

public class UnknownTemplateException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownTemplateException() {
		super("Unknown Template");
	}
}
