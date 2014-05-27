package cdar.bll.entity;

/**
 * Class representing a User and its personal settings.
 *
 * @author dzigerli
 * @author mtinner
 */
public class User extends BasicEntity {
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	/** The accesstoken. */
	private String accesstoken;
	
	/** The treeaccess. */
	private boolean treeaccess;
	
	/** The drill hierarchy. */
	private int drillHierarchy;

	/**
	 * Default Constructor.
	 */
	public User() {

	}

	/**
	 * Constructor passing username and password of the user.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	/**
	 * Gets the username.
	 *
	 * @return username as String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username to be set (String)
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return password as String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password to be set (String)
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the accesstoken.
	 *
	 * @return accesstoken as String
	 */
	public String getAccesstoken() {
		return accesstoken;
	}

	/**
	 * Sets the accesstoken.
	 *
	 * @param accesstoken to be set (String)
	 */
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	/**
	 * Checks if is treeaccess.
	 *
	 * @return boolean value which represents if user has access to the specific tree
	 */
	public boolean isTreeaccess() {
		return treeaccess;
	}

	/**
	 * Sets the treeaccess.
	 *
	 * @param treeaccess to be set as boolean, true if user has access to the specific tree, false if not
	 */
	public void setTreeaccess(boolean treeaccess) {
		this.treeaccess = treeaccess;
	}

	/**
	 * Gets the drill hierarchy.
	 *
	 * @return drill hierarchy as int
	 */
	public int getDrillHierarchy() {
		return drillHierarchy;
	}

	/**
	 * Sets the drill hierarchy.
	 *
	 * @param drillHierarchy as int to be set
	 */
	public void setDrillHierarchy(int drillHierarchy) {
		this.drillHierarchy = drillHierarchy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
