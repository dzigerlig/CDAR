package ch.cdar.bll.wiki;

import java.util.concurrent.CountDownLatch;

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

public class SubnodeCopyHelper extends Thread {
	int uid;
	int nodeId;
	int newNodeId;
	int treeId;
	UserRole userrole;

	public SubnodeCopyHelper(int uid, int nodeId, int newNodeId,
			UserRole userrole) {
		super();
		this.uid = uid;
		this.nodeId = nodeId;
		this.newNodeId = newNodeId;
		this.userrole = userrole;
	}

	public SubnodeCopyHelper(int uid, int nodeId, int newNodeId, int treeId,
			UserRole userrole) {
		super();
		this.uid = uid;
		this.nodeId = nodeId;
		this.treeId = treeId;
		this.userrole = userrole;
	}

	public void run() {
		try {
			if (userrole.equals(UserRole.PRODUCER)) {
				new SubnodeManager().copySubnodes(uid, treeId, nodeId,
						newNodeId);
			} else {
				new ProjectSubnodeManager()
						.copySubnodes(uid, nodeId, newNodeId);
			}
		} catch (UnknownProjectNodeException | EntityException
				| UnknownProjectNodeLinkException | CreationException
				| UnknownUserException | UnknownProjectSubnodeException
				| InterruptedException | UnknownNodeException | UnknownSubnodeException | UnknownTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
