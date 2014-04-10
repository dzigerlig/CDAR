package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.consumer.CommentDao;

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

	public Comment(CommentDao uc) {
		this(uc.getId(), uc.getCreationTime(), uc.getLastModificationTime(), uc.getUid(), uc.getKpnid(), uc.getComment());
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
