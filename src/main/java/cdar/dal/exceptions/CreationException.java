package cdar.dal.exceptions;

public class CreationException extends Exception {
	private static final long serialVersionUID = 1L;

	public CreationException() {
		super("Entity has not been created!");
	}
}
