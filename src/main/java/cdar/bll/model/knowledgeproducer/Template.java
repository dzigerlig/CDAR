package cdar.bll.model.knowledgeproducer;

import java.util.Date;

import cdar.bll.model.WikiEntity;

public class Template extends WikiEntity {
	private int refTreeId;

	public Template() {
		super();
	}

	public Template(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refTreeId) {
		super(id, creationDate, lastModification, title, wikititle);
		this.refTreeId = refTreeId;
	}

	public int getRefTreeId() {
		return refTreeId;
	}

	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
	}	
}
