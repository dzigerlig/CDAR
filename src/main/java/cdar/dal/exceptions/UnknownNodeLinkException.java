package cdar.dal.exceptions;

public class UnknownNodeLinkException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UnknownNodeLinkException() {
		super("Unknown NodeLink");
	}
}
