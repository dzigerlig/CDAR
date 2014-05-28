package ch.cdar.bll.entity;

/**
 * Class of a NodeLink-Entity which represents the link between two specified Nodes.
 *
 * @author dzigerli
 * @authro mtinner
 */
public class NodeLink extends BasicEntity {
	
	/** The source id. */
	private int sourceId;
	
	/** The target id. */
	private int targetId;
	
	/** The subnode id. */
	private int subnodeId;
	
	/** The tree id. */
	private int treeId;
	
	/**
	 * Default Constructor which calls the constructor of the BasicEntity.
	 */
	public NodeLink() {
		super();
	}
	
	/**
	 * Gets the source id.
	 *
	 * @return source id the specified Source-Node
	 */
	public int getSourceId() {
		return sourceId;
	}
	
	/**
	 * Sets the source id.
	 *
	 * @param sourceId of the source node to be set as int value
	 */
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * Gets the target id.
	 *
	 * @return target id of the specified Target-Node
	 */
	public int getTargetId() {
		return targetId;
	}
	
	/**
	 * Sets the target id.
	 *
	 * @param targetId of the target node to be set as int value
	 */
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	/**
	 * This id can be 0 if no Subnode is defined.
	 *
	 * @return subnode id of the specified subnode
	 */
	public int getSubnodeId() {
		return subnodeId;
	}

	/**
	 * Sets the subnode id.
	 *
	 * @param subnodeId of the subnode to be set as int value, passing 0 if no subnode is defined
	 */
	public void setSubnodeId(int subnodeId) {
		this.subnodeId = subnodeId;
	}

	/**
	 * Gets the tree id.
	 *
	 * @return tree id of the specified tree
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId of the tree to be set as int Value
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
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
		result = prime * result + subnodeId;
		result = prime * result + treeId;
		result = prime * result + sourceId;
		result = prime * result + targetId;
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
		if (!(obj instanceof NodeLink))
			return false;
		NodeLink other = (NodeLink) obj;
		
		
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
		if (subnodeId != other.subnodeId)
			return false;
		if (treeId != other.treeId)
			return false;
		if (sourceId != other.sourceId)
			return false;
		if (targetId != other.targetId)
			return false;
		return true;
	}	
}