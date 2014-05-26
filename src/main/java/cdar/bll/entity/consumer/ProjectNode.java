package cdar.bll.entity.consumer;

import cdar.bll.entity.Node;

public class ProjectNode extends Node {
	private int status;
	private int inheritedTreeId;
	
	public ProjectNode() {
		super();
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
