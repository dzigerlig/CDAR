package cdar.dal.exceptions;

public class WikiCreateUserException extends Exception {
	private static final long serialVersionUID = 1L;

	public WikiCreateUserException() {
		super("Wiki User Creation Failed!");
	}
}
