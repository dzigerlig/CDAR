package ch.cdar.bll.helpers;

import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.manager.consumer.ProjectSubnodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class SubnodeCopyHelper.
 * This class is used for creating the subnode wiki pages in an own thread to reduce the waiting time of the client if he copies a node
 */
public class SubnodeCopyHelper extends Thread {
	/** The uid. */
	private int uid;
	
	/** The node id. */
	private int nodeId;
	
	/** The new node id. */
	private int newNodeId;
	
	/** The tree id. */
	private int treeId;
	
	/** The userrole. */
	private UserRole userrole;

	/**
	 * Instantiates a new subnode copy helper.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @param newNodeId the new node id
	 * @param userrole the userrole
	 */
	public SubnodeCopyHelper(int uid, int nodeId, int newNodeId,
			UserRole userrole) {
		super();
		setUid(uid);
		setNodeId(nodeId);
		setNewNodeId(newNodeId);
		setUserrole(userrole);
	}

	/**
	 * Instantiates a new subnode copy helper.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @param newNodeId the new node id
	 * @param treeId the tree id
	 * @param userrole the userrole
	 */
	public SubnodeCopyHelper(int uid, int nodeId, int newNodeId, int treeId,
			UserRole userrole) {
		super();
		setUid(uid);
		setNodeId(nodeId);
		setNewNodeId(newNodeId);
		setTreeId(treeId);
		setUserrole(userrole);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			if (getUserrole().equals(UserRole.PRODUCER)) {
				new SubnodeManager().copySubnodes(getUid(), getTreeId(), getNodeId(),
						getNewNodeId());
			} else {
				new ProjectSubnodeManager()
						.copySubnodes(getUid(), getNodeId(), getNewNodeId());
			}
		} catch (UnknownProjectNodeException | EntityException
				| UnknownProjectNodeLinkException | CreationException
				| UnknownUserException | UnknownProjectSubnodeException
				| InterruptedException | UnknownNodeException | UnknownSubnodeException | UnknownTreeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 *
	 * @param nodeId the new node id
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the new node id.
	 *
	 * @return the new node id
	 */
	public int getNewNodeId() {
		return newNodeId;
	}

	/**
	 * Sets the new node id.
	 *
	 * @param newNodeId the new new node id
	 */
	public void setNewNodeId(int newNodeId) {
		this.newNodeId = newNodeId;
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
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * Gets the userrole.
	 *
	 * @return the userrole
	 */
	public UserRole getUserrole() {
		return userrole;
	}

	/**
	 * Sets the userrole.
	 *
	 * @param userrole the new userrole
	 */
	public void setUserrole(UserRole userrole) {
		this.userrole = userrole;
	}
}
