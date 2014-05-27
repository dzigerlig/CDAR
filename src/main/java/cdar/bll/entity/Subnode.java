package cdar.bll.entity;

import java.util.Date;

/**
 * The Class Subnode.
 */
public class Subnode extends WikiEntity {
	
	/** The node id. */
	private int nodeId;
	
	/** The position. */
	private int position;

	/**
	 * Instantiates a new subnode.
	 */
	public Subnode() {
		super();
	}

	/**
	 * Instantiates a new subnode.
	 *
	 * @param id the id
	 * @param creationDate the creation date
	 * @param lastModification the last modification
	 * @param title the title
	 * @param wikititle the wikititle
	 * @param nodeId the node id
	 * @param position the position
	 */
	public Subnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeId, int position) {
		super(id, creationDate, lastModification, title, wikititle);
		setNodeId(nodeId);
		setPosition(position);
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 *
	 * @param nodeId the new node id
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
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
		result = prime * result + nodeId;
		result = prime * result + position;
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
		if (!(obj instanceof Subnode))
			return false;
		Subnode other = (Subnode) obj;		

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
		if (nodeId != other.nodeId)
			return false;
		if (position != other.position)
			return false;
		return true;
	}
}