package cdar.bll.entity;

import java.util.Date;

/**
 * Default class for every entity, which gets transferred through the HTTP WebAPI.
 *
 * @author dzigerli
 * @author mtinner
 */
public class BasicEntity {
	
	/** The id. */
	private int id;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The last modification time. */
	private Date lastModificationTime;
	
	/**
	 * Default Constructor.
	 */
	public BasicEntity() {
		super();
	}
	
	/**
	 * Constructor to create an object by id.
	 *
	 * @param id integer value of id
	 */
	public BasicEntity(int id) {
		super();
		setId(id);
	}

	/**
	 * Constructor with all parameters.
	 *
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
	 * Gets the id.
	 *
	 * @return current id value as int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id integer value of id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the creation time.
	 *
	 * @return creation time value as Date
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	
	/**
	 * sets creation time.
	 *
	 * @param creationTime Date
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the last modification time.
	 *
	 * @return last modification time as Date
	 */
	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	/**
	 * sets last modification time.
	 *
	 * @param lastModificationTime Date
	 */
	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
}
