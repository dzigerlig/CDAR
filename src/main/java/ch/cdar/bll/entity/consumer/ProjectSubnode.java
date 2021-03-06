package ch.cdar.bll.entity.consumer;

import ch.cdar.bll.entity.Subnode;

/**
 * The Class ProjectSubnode.
 */
public class ProjectSubnode extends Subnode {
	
	/** The status. */
	private int status;
	
	/** The inherited tree id. */
	private int inheritedTreeId;

	/**
	 * Instantiates a new project subnode.
	 */
	public ProjectSubnode() {
		super();
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the inherited tree id.
	 *
	 * @return the inherited tree id
	 */
	public int getInheritedTreeId() {
		return inheritedTreeId;
	}

	/**
	 * Sets the inherited tree id.
	 *
	 * @param inheritedTreeId the new inherited tree id
	 */
	public void setInheritedTreeId(int inheritedTreeId) {
		this.inheritedTreeId = inheritedTreeId;
	}
}
