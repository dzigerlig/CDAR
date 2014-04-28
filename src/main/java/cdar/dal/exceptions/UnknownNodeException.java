package cdar.dal.exceptions;

public class UnknownNodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownNodeException() {
		super("Unknown Node");
	}
}
