package cdar.bll.model.knowledgeproducer;

import java.util.Date;

import cdar.bll.model.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;

public class Dictionary extends BasicEntity {
	private int parentId;
	private int refTreeId;
	private String title;


	public Dictionary() {
		super();
	}

	public Dictionary(int id, Date creationTime, Date lastModificationTime,
			int parentId,int refTreeId, String title) {
		super(id, creationTime, lastModificationTime);
		this.parentId = parentId;
		this.refTreeId = refTreeId;
		this.title = title;
	}

	public Dictionary(DictionaryDao knd) {
		super(knd.getId(), knd.getCreationTime(), knd.getLastModificationTime());		
		this.title = knd.getTitle();	
		if(knd.getParentDictionaryDao()!=null){
			this.parentId = knd.getParentDictionaryDao().getId();
		}	
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getRefTreeId() {
		return refTreeId;
	}

	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
