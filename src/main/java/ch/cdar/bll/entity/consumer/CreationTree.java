package ch.cdar.bll.entity.consumer;

import ch.cdar.bll.entity.Tree;

/**
 * The Class CreationTree is an extension of a tree, containing the inherited tree id of the producer tree
 */
public class CreationTree extends Tree {
	
	/** The copy tree id. */
	private int copyTreeId;

	/**
	 * Instantiates a new creation tree.
	 */
	public CreationTree() {
		super();
	}

	/**
	 * Gets the copy tree id.
	 *
	 * @return the copy tree id
	 */
	public int getCopyTreeId() {
		return copyTreeId;
	}

	/**
	 * Sets the copy tree id.
	 *
	 * @param copyTreeId the new copy tree id
	 */
	public void setCopyTreeId(int copyTreeId) {
		this.copyTreeId = copyTreeId;
	}
}
