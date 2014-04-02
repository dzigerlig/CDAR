package cdar.bll.model.user;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.user.UserDao;

public class User extends BasicEntity {
	private String username;
	private String password;
	private String accesstoken;
	private boolean isProducer;
	
	public User() {
		
	}
	
	public User(int id, Date creationTime, Date lastModificationTime,
			String username, String password, String accessToken) {
		super(id, creationTime, lastModificationTime);
		this.setUsername(username);
		this.setPassword(password);
		this.setAccesstoken(accessToken);
	}
	
	public User(int id) {
		super(id);
	}
	
	public User(String username, String accesstoken, boolean isProducer) {
		setUsername(username);
		setAccesstoken(accesstoken);
		setIsProducer(isProducer);
	}
	
	public User(UserDao userDao) {
		this(userDao.getId(), userDao.getCreationTime(), userDao.getLastModificationTime(), userDao.getUsername(), userDao.getPassword(), userDao.getAccesstoken());
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public boolean getIsProducer() {
		return isProducer;
	}

	public void setIsProducer(boolean isProducer) {
		this.isProducer = isProducer;
	}
}
