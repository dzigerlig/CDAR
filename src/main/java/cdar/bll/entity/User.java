package cdar.bll.entity;

import java.util.Date;

public class User extends BasicEntity {
	private String username;
	private String password;
	private String accesstoken;
	private boolean treeaccess;
	private int drillHierarchy;

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

	public boolean isTreeaccess() {
		return treeaccess;
	}

	public void setTreeaccess(boolean treeaccess) {
		this.treeaccess = treeaccess;
	}

	public int getDrillHierarchy() {
		return drillHierarchy;
	}

	public void setDrillHierarchy(int drillHierarchy) {
		this.drillHierarchy = drillHierarchy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
		result = prime * result + getId();
		result = prime
				* result
				+ ((getLastModificationTime() == null) ? 0 : getLastModificationTime()
						.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		
		if (getCreationTime() == null) {
			if (other.getCreationTime() != null) {
				return false;
			}
		} else if (!getCreationTime().equals(other.getCreationTime())) {
			return false;
		}
		if (getId() != other.getId()) {
			return false;
		}
		if (getLastModificationTime() == null) {
			if (other.getLastModificationTime() != null) {
				return false;
			}
		} else if (!getLastModificationTime().equals(other.getLastModificationTime())) {
			return false;
		}		
		
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
}
