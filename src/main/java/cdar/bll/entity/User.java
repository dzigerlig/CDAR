package cdar.bll.entity;

/**
 * Class representing a User and its personal settings
 * @author dzigerli
 * @author mtinner
 * 
 */
public class User extends BasicEntity {
	private String username;
	private String password;
	private String accesstoken;
	private boolean treeaccess;
	private int drillHierarchy;

	/**
	 * Default Constructor
	 */
	public User() {

	}

	/**
	 * Constructor passing username and password of the user
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	/**
	 * 
	 * @return username as String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username to be set (String)
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return password as String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password to be set (String)
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return accesstoken as String
	 */
	public String getAccesstoken() {
		return accesstoken;
	}

	/**
	 * 
	 * @param accesstoken to be set (String)
	 */
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	/**
	 * 
	 * @return boolean value which represents if user has access to the specific tree
	 */
	public boolean isTreeaccess() {
		return treeaccess;
	}

	/**
	 * 
	 * @param treeaccess to be set as boolean, true if user has access to the specific tree, false if not
	 */
	public void setTreeaccess(boolean treeaccess) {
		this.treeaccess = treeaccess;
	}

	/**
	 * 
	 * @return drill hierarchy as int
	 */
	public int getDrillHierarchy() {
		return drillHierarchy;
	}

	/**
	 * 
	 * @param drillHierarchy as int to be set
	 */
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
