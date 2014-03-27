package cdar.bll.model.knowledgeproducer;

import java.util.Date;

import cdar.bll.model.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;

public class Dictionary extends BasicEntity {
	private int parentId;
	private String title;


	public Dictionary() {
		super();
	}

	public Dictionary(int id, Date creationTime, Date lastModificationTime,
			int parentId, String title) {
		super(id, creationTime, lastModificationTime);
		this.parentId = parentId;
		this.title = title;
	}

	public Dictionary(DictionaryDao knd) {
		super(knd.getId(), knd.getCreationTime(), knd.getLastModificationTime());
		this.parentId = knd.getParentId();
		this.title = knd.getTitle();	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
