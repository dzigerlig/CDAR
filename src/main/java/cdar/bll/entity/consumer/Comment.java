package cdar.bll.entity.consumer;

import cdar.bll.entity.BasicEntity;

/**
 * The Class Comment.
 */
public class Comment extends BasicEntity {
	
	/** The user id. */
	private int userId;
	
	/** The node id. */
	private int nodeId;
	
	/** The comment. */
	private String comment;
	
	/** The username. */
	private String username;
	
	/**
	 * Instantiates a new comment.
	 */
	public Comment() {
		super();
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
