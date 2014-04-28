package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;

public class ProjectSubnode extends WikiEntity {
	private int refProjectNodeId;
	private int position;

	public ProjectSubnode() {
		super();
	}
	
	public ProjectSubnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refProjectNodeId) {
		
		super(id, creationDate, lastModification, title, wikititle);
		
		setRefProjectNodeId(refProjectNodeId);
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
}
