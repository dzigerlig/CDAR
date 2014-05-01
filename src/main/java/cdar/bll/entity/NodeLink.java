package cdar.bll.entity;

import java.util.Date;

public class NodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int subnodeId;
	private int treeId;
	
	public NodeLink() {
		super();
	}
	
	public NodeLink(int id, Date creationDate, Date lastModification,
			int sourceId, int targetId, int subnodeId, int treeId) {
		super(id, creationDate, lastModification);
		setSourceId(sourceId);
		setTargetId(targetId);
		setSubnodeId(subnodeId);
		setTreeId(treeId);
	}
	
	public int getSourceId() {
		return sourceId;
	}
	
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public int getSubnodeId() {
		return subnodeId;
	}

	public void setSubnodeId(int subnodeId) {
		this.subnodeId = subnodeId;
	}

	public int getTreeId() {
		return treeId;
	}

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

