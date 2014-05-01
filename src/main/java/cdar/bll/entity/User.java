package cdar.bll.entity;

import java.util.Date;

import com.owlike.genson.annotation.JsonIgnore;

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
