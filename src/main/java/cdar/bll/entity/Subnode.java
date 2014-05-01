package cdar.bll.entity;

import java.util.Date;

public class Subnode extends WikiEntity {
	private int nodeId;
	private int position;

	public Subnode() {
		super();
	}

	public Subnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeId, int position) {
		super(id, creationDate, lastModification, title, wikititle);
		setNodeId(nodeId);
		setPosition(position);
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result
				+ ((getWikititle() == null) ? 0 : getWikititle().hashCode());
		result = prime * result + nodeId;
		result = prime * result + position;
		return result;
	}

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