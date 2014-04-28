package cdar.dal.exceptions;

public class UnknownProjectSubnodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownProjectSubnodeException() {
		super("Unknown Project Subnode");
	}
}
