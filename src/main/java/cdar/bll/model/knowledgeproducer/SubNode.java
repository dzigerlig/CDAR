package cdar.bll.model.knowledgeproducer;

import java.util.Date;

import cdar.bll.model.WikiEntity;

public class SubNode extends WikiEntity {
	private int refNodeId;

	public SubNode() {
		super();
	}

	public SubNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refNodeId) {
		super(id, creationDate, lastModification, title, wikititle);
		this.refNodeId = refNodeId;
	}

	public int getRefNodeId() {
		return refNodeId;
	}

	public void setRefNodeId(int refNodeId) {
		this.refNodeId = refNodeId;
	}
}
