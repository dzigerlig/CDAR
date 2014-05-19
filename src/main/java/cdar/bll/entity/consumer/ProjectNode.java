package cdar.bll.entity.consumer;

import java.util.Date;

import cdar.bll.entity.Node;

public class ProjectNode extends Node {
	private int status;
	private int inheritedTreeId;
	
	public ProjectNode() {
		super();
	}

	public ProjectNode(int id, Date creationDate, Date lastModificationDate,
			String title, String wikititle, int treeId, int dynamicTreeFlag, int directoryId, int nodeStatus) {
		super(id, creationDate, lastModificationDate, title, wikititle, treeId, dynamicTreeFlag, directoryId);
		setStatus(nodeStatus);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getInheritedTreeId() {
		return inheritedTreeId;
	}

	public void setInheritedTreeId(int inheritedTreeId) {
		this.inheritedTreeId = inheritedTreeId;
	}
}
