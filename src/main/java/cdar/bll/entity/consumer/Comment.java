package cdar.bll.entity.consumer;

import java.util.Date;

import cdar.bll.entity.BasicEntity;

public class Comment extends BasicEntity {
	private int userId;
	private int nodeId;
	private String comment;
	
	public Comment() {
		super();
	}

	public Comment(int id, Date creationDate, Date lastModification,
			int userId, int nodeId, String comment) {
		super(id, creationDate, lastModification);
		setUserId(userId);
		setNodeId(nodeId);
		setComment(comment);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
