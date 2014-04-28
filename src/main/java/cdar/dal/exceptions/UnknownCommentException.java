package cdar.dal.exceptions;

public class UnknownCommentException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownCommentException() {
		super("Unknown Comment");
	}
}
