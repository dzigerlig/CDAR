package cdar.dal.exceptions;

public class WikiLoginException extends Exception {
	private static final long serialVersionUID = 1L;

	public WikiLoginException() {
		super("Wiki Login Failed");
	}
}
