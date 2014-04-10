package cdar.bll.user;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.user.UserDao;

public class User extends BasicEntity {
	private String username;
	private String password;
	private String accesstoken;
	
	public User() {
		
	}
	
	public User(int id, Date creationTime, Date lastModificationTime,
			String username, String password, String accessToken) {
		super(id, creationTime, lastModificationTime);
		setUsername(username);
		setPassword(password);
		setAccesstoken(accessToken);
	}
	
	public User(int id) {
		super(id);
	}
	
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
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
}