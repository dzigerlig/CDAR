package cdar.dal.exceptions;

public class WrongCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;

	public WrongCredentialsException() {
		super("Wrong user credentials");
	}
}
