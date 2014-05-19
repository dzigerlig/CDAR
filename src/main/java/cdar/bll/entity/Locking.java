package cdar.bll.entity;

import java.util.Calendar;
import java.util.Date;

import cdar.PropertyHelper;

public class Locking {	
	private int userId;
	private int treeId;
	private boolean isProducer;
	private Date lockingTime;
	
	public Locking() {
		super();
	}

	public Locking(int userId, int treeId, boolean isProducer) {
		super();
		setUserId(userId);
		setTreeId(treeId);
		setProducer(isProducer);
		setLockingTime(lockingTime = new Date());
		renewLockingTime();
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	public boolean isProducer() {
		return isProducer;
	}

	public void setProducer(boolean isProducer) {
		this.isProducer = isProducer;
	}

	public Date getLockingTime() {
		return lockingTime;
	}

	public void setLockingTime(Date lockingTime) {
		this.lockingTime = lockingTime;
	}

	public void renewLockingTime() {
		PropertyHelper property = new PropertyHelper();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(property.getProperty("LOCKING_HOUR")));
	    calendar.add(Calendar.MINUTE, Integer.valueOf(property.getProperty("LOCKING_MINUTE")));
		setLockingTime(calendar.getTime());
	}

	public boolean isExpired() {
		return getLockingTime().before(new Date());
	}
}
