package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;

public class ProjectNode extends WikiEntity {
	private int status;
	private int refProjectTreeId;
	
	public ProjectNode() {
		super();
	}

	public ProjectNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeStatus, int refProjectTreeId) {
		super(id, creationDate, lastModification, title, wikititle);
		setStatus(nodeStatus);
		setRefProjectTreeId(refProjectTreeId);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRefProjectTreeId() {
		return refProjectTreeId;
	}

	public void setRefProjectTreeId(int refProjectTreeId) {
		this.refProjectTreeId = refProjectTreeId;
	}
	
}
