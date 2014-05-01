package cdar.bll.entity;

import java.util.Date;

public class BasicEntity {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	
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
		setLastModificationTime(lastModificationTime);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}	
	
}
