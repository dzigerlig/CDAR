package cdar.dal.exceptions;

public class UnknownEntityException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownEntityException() {
		super("Unknown Entity");
	}
}
