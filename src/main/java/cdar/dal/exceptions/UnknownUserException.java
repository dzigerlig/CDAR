package cdar.dal.exceptions;

public class UnknownUserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnknownUserException() {
		super("Unknown user");
	}
}
