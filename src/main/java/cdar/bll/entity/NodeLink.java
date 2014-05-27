package cdar.bll.entity;

/**
 * Class of a NodeLink-Entity which represents the link between two specified Nodes
 * @author dzigerli
 * @authro mtinner
 *
 */
public class NodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int subnodeId;
	private int treeId;
	
	/**
	 * Default Constructor which calls the constructor of the BasicEntity
	 */
	public NodeLink() {
		super();
	}
	
	/**
	 * 
	 * @return source id the specified Source-Node
	 */
	public int getSourceId() {
		return sourceId;
	}
	
	/**
	 * 
	 * @param sourceId of the source node to be set as int value
	 */
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * 
	 * @return target id of the specified Target-Node
	 */
	public int getTargetId() {
		return targetId;
	}
	
	/**
	 * 
	 * @param targetId of the target node to be set as int value
	 */
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	/**
	 * This id can be 0 if no Subnode is defined
	 * @return subnode id of the specified subnode
	 */
	public int getSubnodeId() {
		return subnodeId;
	}

	/**
	 * 
	 * @param subnodeId of the subnode to be set as int value, passing 0 if no subnode is defined
	 */
	public void setSubnodeId(int subnodeId) {
		this.subnodeId = subnodeId;
	}

	/**
	 * 
	 * @return tree id of the specified tree
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * 
	 * @param treeId of the tree to be set as int Value
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

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

