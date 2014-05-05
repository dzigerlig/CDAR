package cdar.dal.exceptions;

public class UsernameInvalidException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsernameInvalidException() {
		super("This username is invalid");
	}
}
