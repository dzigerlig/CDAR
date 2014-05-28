package cdar.bll.reporting;

import java.util.Date;

import cdar.bll.entity.CdarDescriptions;
import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

/**
 * The Class ReportingBean.
 */
public class ReportingBean {
	
	/** The tree id. */
	private int treeId;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The cdar descriptions. */
	private CdarDescriptions cdarDescriptions;
	
	/** The username. */
	private String username;
	
	/**
	 * Instantiates a new reporting bean.
	 *
	 * @throws Exception the exception
	 */
	public ReportingBean() throws Exception {
		setCreationTime(new Date());
		setCdarDescriptions(new CdarDescriptions());
	}
	
	/**
	 * Check credentials.
	 *
	 * @param uid the uid
	 * @param accesstoken the accesstoken
	 * @throws Exception the exception
	 */
	public void checkCredentials(int uid, String accesstoken) throws Exception {
		UserManager um = new UserManager();
		User user = um.getUser(uid);

		if (!user.getAccesstoken().equals(accesstoken)) {
			throw new UnknownUserException();
		}
		
		setUsername(user.getUsername());
	}

	/**
	 * Gets the tree id.
	 *
	 * @return the tree id
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId the new tree id
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
	public void setTreeId(int treeId) throws UnknownTreeException,
			EntityException, UnknownNodeException, UnknownUserException,
			UnknownSubnodeException {
		this.treeId = treeId;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the cdar descriptions.
	 *
	 * @return the cdar descriptions
	 */
	public CdarDescriptions getCdarDescriptions() {
		return cdarDescriptions;
	}

	/**
	 * Sets the cdar descriptions.
	 *
	 * @param cdarDescriptions the new cdar descriptions
	 */
	public void setCdarDescriptions(CdarDescriptions cdarDescriptions) {
		this.cdarDescriptions = cdarDescriptions;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
