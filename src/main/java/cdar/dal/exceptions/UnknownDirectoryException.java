package cdar.dal.exceptions;

public class UnknownDirectoryException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownDirectoryException() {
		super("Unknown Directory");
	}
}
