package cdar.bll.model.knowledgeproducer;

import java.util.Date;

import cdar.bll.model.BasicEntity;

public class Dictionary extends BasicEntity {
	private int parentId;


	public Dictionary() {
		super();
	}

	public Dictionary(int id, Date creationTime, Date lastModificationTime,
			int parentId) {
		super(id, creationTime, lastModificationTime);
		this.parentId = parentId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
