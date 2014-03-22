package cdar.bll.model.knowledgeconsumer;

import java.util.Date;

import cdar.bll.model.WikiEntity;

public class ProjectSubNode extends WikiEntity {
	private int refProjectNodeId;

	public ProjectSubNode() {
		super();
	}
	
	public ProjectSubNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refProjectNodeId) {
		super(id, creationDate, lastModification, title, wikititle);
		this.setRefProjectNodeId(refProjectNodeId);
	}

	public int getRefProjectNodeId() {
		return refProjectNodeId;
	}

	public void setRefProjectNodeId(int refProjectNodeId) {
		this.refProjectNodeId = refProjectNodeId;
	}
}
