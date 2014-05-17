package cdar.bll.exceptions;

public class LockingException extends Exception {

	private static final long serialVersionUID = 1L;

	public LockingException() {
		super("Object already locked");
	}
}
