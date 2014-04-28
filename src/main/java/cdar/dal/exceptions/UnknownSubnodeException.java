package cdar.dal.exceptions;

public class UnknownSubnodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownSubnodeException() {
		super("Unknown Subnode");
	}
}
