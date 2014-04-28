package cdar.dal.exceptions;

public class UnknownProjectTreeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownProjectTreeException() {
		super("Unknown Project Tree");
	}
}
