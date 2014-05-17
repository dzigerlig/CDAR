package cdar.bll.entity;

import java.util.Date;

import cdar.dal.PropertyHelper;

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
		this.userId = userId;
		this.treeId = treeId;
		this.isProducer = isProducer;
		this.lockingTime = new Date();
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

	@SuppressWarnings("deprecation")
	public void renewLockingTime() {
		PropertyHelper property = new PropertyHelper();
		Date d = new Date();
		d.setHours(d.getHours()
				+ Integer.valueOf(property.getProperty("LOCKING_HOUR")));
		d.setMinutes(d.getMinutes()
				+ Integer.valueOf(property.getProperty("LOCKING_MINUTE")));
		setLockingTime(d);
	}
	

	public boolean isExpired() {
		return getLockingTime().before(new Date());
	}

}
