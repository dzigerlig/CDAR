package cdar.bll.entity;

import java.util.Date;

public class Node extends WikiEntity {
	private int treeId;
	private int dynamicTreeFlag;
	private int directoryId;

	public Node() {
		super();
	}

	public Node(int id, Date creationDate, Date lastModification, String title,
			String wikititle, int treeId, int dynamicTreeFlag, int directoryId) {
		super(id, creationDate, lastModification, title, wikititle);
		setTreeId(treeId);
		setDynamicTreeFlag(dynamicTreeFlag);
		setDirectoryId(directoryId);
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	public int getDynamicTreeFlag() {
		return dynamicTreeFlag;
	}

	public void setDynamicTreeFlag(int dynamicTreeFlag) {
		this.dynamicTreeFlag = dynamicTreeFlag;
	}

	public int getDirectoryId() {
		return directoryId;
	}

	public void setDirectoryId(int directoryId) {
		this.directoryId = directoryId;
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
		result = prime * result + directoryId;
		result = prime * result + dynamicTreeFlag;
		result = prime * result + treeId;
		return result;
	}

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
