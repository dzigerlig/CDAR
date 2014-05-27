package cdar.bll.entity;

import java.util.Date;

/**
 * The Class Node.
 */
public class Node extends WikiEntity {
	
	/** The tree id. */
	private int treeId;
	
	/** The dynamic tree flag. */
	private int dynamicTreeFlag;
	
	/** The directory id. */
	private int directoryId;

	/**
	 * Instantiates a new node.
	 */
	public Node() {
		super();
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param id the id
	 * @param creationDate the creation date
	 * @param lastModification the last modification
	 * @param title the title
	 * @param wikititle the wikititle
	 * @param treeId the tree id
	 * @param dynamicTreeFlag the dynamic tree flag
	 * @param directoryId the directory id
	 */
	public Node(int id, Date creationDate, Date lastModification, String title,
			String wikititle, int treeId, int dynamicTreeFlag, int directoryId) {
		super(id, creationDate, lastModification, title, wikititle);
		setTreeId(treeId);
		setDynamicTreeFlag(dynamicTreeFlag);
		setDirectoryId(directoryId);
	}

	/**
	 * Gets the tree id.
	 *
	 * @return the tree id
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId the new tree id
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * Gets the dynamic tree flag.
	 *
	 * @return the dynamic tree flag
	 */
	public int getDynamicTreeFlag() {
		return dynamicTreeFlag;
	}

	/**
	 * Sets the dynamic tree flag.
	 *
	 * @param dynamicTreeFlag the new dynamic tree flag
	 */
	public void setDynamicTreeFlag(int dynamicTreeFlag) {
		this.dynamicTreeFlag = dynamicTreeFlag;
	}

	/**
	 * Gets the directory id.
	 *
	 * @return the directory id
	 */
	public int getDirectoryId() {
		return directoryId;
	}

	/**
	 * Sets the directory id.
	 *
	 * @param directoryId the new directory id
	 */
	public void setDirectoryId(int directoryId) {
		this.directoryId = directoryId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result
				+ ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
		result = prime * result + getId();
		result = prime
				* result
				+ ((getLastModificationTime() == null) ? 0 : getLastModificationTime()
						.hashCode());		
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result
				+ ((getWikititle() == null) ? 0 : getWikititle().hashCode());	
		result = prime * result + directoryId;
		result = prime * result + dynamicTreeFlag;
		result = prime * result + treeId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;

		if (getCreationTime() == null) {
			if (other.getCreationTime() != null)
				return false;
		} else if (!getCreationTime().equals(other.getCreationTime()))
			return false;
		if (getId() != other.getId())
			return false;
		if (getLastModificationTime() == null) {
			if (other.getLastModificationTime() != null)
				return false;
		} else if (!getLastModificationTime().equals(
				other.getLastModificationTime()))
			return false;
		if (getTitle() == null) {
			if (other.getTitle() != null)
				return false;
		} else if (!getTitle().equals(other.getTitle()))
			return false;
		if (getWikititle() == null) {
			if (other.getWikititle() != null)
				return false;
		} else if (!getWikititle().equals(other.getWikititle()))
			return false;
		else if (directoryId != other.directoryId)
			return false;
		else if (dynamicTreeFlag != other.dynamicTreeFlag)
			return false;
		else if (treeId != other.treeId)
			return false;
		return true;
	}
}
