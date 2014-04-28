package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;

public class ProjectNode extends WikiEntity {
	private int nodeStatus;
	private int refProjectTreeId;
	
	public ProjectNode() {
		super();
	}

	public ProjectNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeStatus, int refProjectTreeId) {
		super(id, creationDate, lastModification, title, wikititle);
		setNodeStatus(nodeStatus);
		setRefProjectTreeId(refProjectTreeId);
	}

	public int getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(int nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public int getRefProjectTreeId() {
		return refProjectTreeId;
	}

	public void setRefProjectTreeId(int refProjectTreeId) {
		this.refProjectTreeId = refProjectTreeId;
	}
	
}
