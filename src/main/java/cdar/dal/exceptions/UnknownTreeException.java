package cdar.dal.exceptions;

public class UnknownTreeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownTreeException() {
		super("Unknown Tree");
	}
}
