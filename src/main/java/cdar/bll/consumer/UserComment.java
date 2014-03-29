package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;

public class UserComment extends BasicEntity {
	private int refUserId;
	private int refProjectNodeId;
	
	public UserComment() {
		super();
	}

	public UserComment(int id, Date creationDate, Date lastModification,
			int refUserId, int refProjectNode) {
		super(id, creationDate, lastModification);
		this.setRefUserId(refUserId);
		this.setRefProjectNode(refProjectNode);
	}

	public int getRefUserId() {
		return refUserId;
	}

	public void setRefUserId(int refUserId) {
		this.refUserId = refUserId;
	}
	
	public int getRefProjectNode() {
		return refProjectNodeId;
	}

	public void setRefProjectNode(int refProjectNode) {
		this.refProjectNodeId = refProjectNode;
	}

	
}
