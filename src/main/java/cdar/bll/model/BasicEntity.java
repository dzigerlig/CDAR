package cdar.bll.model;

import java.util.Date;

public class BasicEntity {
	private int id;
	private Date creationTime;
	private Date lastModification;
	
	public BasicEntity() {
		super();
	}
	
	public BasicEntity(int id) {
		super();
		setId(id);
	}

	public BasicEntity(int id, Date creationTime, Date lastModificationTime) {
		super();
		setId(id);
		setCreationTime(creationTime);
		setLastModified(lastModificationTime);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getCreationDate() {
		return creationTime;
	}
	
	public void setCreationTime(Date created) {
		this.creationTime = created;
	}
	
	public Date getLastModified() {
		return lastModification;
	}
	
	public void setLastModified(Date modified) {
		this.lastModification = modified;
	}
}
