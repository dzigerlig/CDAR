package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;

public class Comment extends BasicEntity {
	private int refUserId;
	private int refProjectNodeId;
	private String comment;
	
	public Comment() {
		super();
	}

	public Comment(int id, Date creationDate, Date lastModification,
			int refUserId, int refProjectNode, String comment) {
		super(id, creationDate, lastModification);
		setRefUserId(refUserId);
		setRefProjectNode(refProjectNode);
		setComment(comment);
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
