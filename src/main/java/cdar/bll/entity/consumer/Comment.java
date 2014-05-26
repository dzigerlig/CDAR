package cdar.bll.entity.consumer;

import cdar.bll.entity.BasicEntity;

public class Comment extends BasicEntity {
	private int userId;
	private int nodeId;
	private String comment;
	private String username;
	
	public Comment() {
		super();
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
