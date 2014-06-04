package ch.cdar.bll.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ch.cdar.bll.entity.Descriptions;
import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownDirectoryException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class ReportingBean.
 */
public class ReportingBean {
	/** The tree id. */
	private int treeId;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The descriptions. */
	private Descriptions cdarDescriptions;
	
	/** The username. */
	private String username;
	
	private DirectoryManager dm;
	
	/**
	 * Instantiates a new reporting bean.
	 *
	 * @throws Exception the exception
	 */
	public ReportingBean(UserRole userRole) throws Exception {
		setCreationTime(new Date());
		setCdarDescriptions(new Descriptions());
		dm = new DirectoryManager(userRole);
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
	public Descriptions getCdarDescriptions() {
		return cdarDescriptions;
	}

	/**
	 * Sets the cdar descriptions.
	 *
	 * @param cdarDescriptions the new cdar descriptions
	 */
	public void setCdarDescriptions(Descriptions cdarDescriptions) {
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
	
	public String getDirectoryBreadcrumb(String treeTitle, int directoryId) throws UnknownDirectoryException, EntityException, UnknownTreeException, UnknownProjectTreeException, UnknownUserException {
		try {
		Directory directory = dm.getDirectory(directoryId);
		ArrayList<String> directoryNames = new ArrayList<String>();
		//get all parents
		while (directory.getParentId()!=0) {
			directoryNames.add(directory.getTitle());
			directory = dm.getDirectory(directory.getParentId());
		}
		
		StringBuilder sb = new StringBuilder(); 
		sb.append(treeTitle);
		
		Collections.reverse(directoryNames);
		
		for (String directoryName : directoryNames) {
			sb.append(" > ");
			sb.append(directoryName);
		}
		
		return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
