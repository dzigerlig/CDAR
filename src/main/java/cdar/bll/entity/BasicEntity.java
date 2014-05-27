package cdar.bll.entity;

import java.util.Date;

/**
 * Default class for every entity, which gets transferred through the HTTP WebAPI
 * 
 * @author dzigerli
 * @author mtinner
 *
 */
public class BasicEntity {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	
	/**
	 * Default Constructor
	 */
	public BasicEntity() {
		super();
	}
	
	/**
	 * Constructor to create an object by id
	 * @param id integer value of id
	 */
	public BasicEntity(int id) {
		super();
		setId(id);
	}

	/**
	 * Constructor with all parameters
	 * @param id integer value of id
	 * @param creationTime value of creation time as Date
	 * @param lastModificationTime value of last modification time as Date
	 */
	public BasicEntity(int id, Date creationTime, Date lastModificationTime) {
		super();
		setId(id);
		setCreationTime(creationTime);
		setLastModificationTime(lastModificationTime);
	}
	
	/**
	 * 
	 * @return current id value as int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id integer value of id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return creation time value as Date
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	
	/**
	 * sets creation time
	 * @param creationTime Date
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * 
	 * @return last modification time as Date
	 */
	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	/**
	 * sets last modification time
	 * @param lastModificationTime Date
	 */
	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
}
