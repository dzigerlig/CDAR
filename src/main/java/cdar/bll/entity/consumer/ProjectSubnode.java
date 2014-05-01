package cdar.bll.entity.consumer;

import java.util.Date;

import cdar.bll.entity.Subnode;

public class ProjectSubnode extends Subnode {
	private int refProjectNodeId;
	private int position;
	private int status;

	public ProjectSubnode() {
		super();
	}
	
	public ProjectSubnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int projectNodeId, int position, int status) {
		super(id, creationDate, lastModification, title, wikititle, projectNodeId, position);
		setStatus(status);
	}

	public int getRefProjectNodeId() {
		return refProjectNodeId;
	}

	public void setRefProjectNodeId(int refProjectNodeId) {
		this.refProjectNodeId = refProjectNodeId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
