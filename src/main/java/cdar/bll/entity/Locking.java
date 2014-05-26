package cdar.bll.entity;

import java.util.Calendar;
import java.util.Date;

import cdar.dal.helpers.PropertyHelper;

/**
 * Class used for locking, to prevent data overrides when multiple users are trying to change the same tree at the same time
 * @author dani
 *
 */
public class Locking {	
	private int userId;
	private int treeId;
	private boolean isProducer;
	private Date lockingTime;
	
	/**
	 * Default constructor
	 */
	public Locking() {
		super();
	}

	/**
	 * @param userId id of the user who is editing (int)
	 * @param treeId id of the tree which the user is trying to edit (int)
	 * @param isProducer as a boolean to check if the user is in role producer or consumer
	 */
	public Locking(int userId, int treeId, boolean isProducer) {
		super();
		setUserId(userId);
		setTreeId(treeId);
		setProducer(isProducer);
		setLockingTime(new Date());
		renewLockingTime();
	}

	/**
	 * 
	 * @return id of the user as int
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId as int to be set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return id of the tree as int
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * 
	 * @param treeId as int to be set
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * 
	 * @return boolean value representing the user state, true if user is in role producer, false if user is in role consumer
	 */
	public boolean isProducer() {
		return isProducer;
	}

	/**
	 * 
	 * @param isProducer boolean value, true if user is in role producer, false if user is in role consumer
	 */
	public void setProducer(boolean isProducer) {
		this.isProducer = isProducer;
	}

	/**
	 * 
	 * @return locking time as a Date value
	 */
	public Date getLockingTime() {
		return lockingTime;
	}

	/**
	 * 
	 * @param lockingTime new date value of the locking time to be set
	 */
	public void setLockingTime(Date lockingTime) {
		this.lockingTime = lockingTime;
	}

	/**
	 * Gets the time of a lock out of the property file and adds it to the current date
	 */
	public void renewLockingTime() {
		PropertyHelper property = new PropertyHelper();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(property.getProperty("LOCKING_HOUR")));
	    calendar.add(Calendar.MINUTE, Integer.valueOf(property.getProperty("LOCKING_MINUTE")));
		setLockingTime(calendar.getTime());
	}

	/**
	 * 
	 * @return boolean value which tells if the lock is expired (true) or not (false)
	 */
	public boolean isExpired() {
		return getLockingTime().before(new Date());
	}
}
