package cdar.bll.entity.consumer;

import java.util.Date;

import cdar.bll.entity.Tree;

public class CreationTree extends Tree {
	private int copyTreeId;

	public CreationTree(int copyTreeId, int id, Date creationDate,
			Date lastModification, String title) {
		super(id, creationDate, lastModification, title);
		setCopyTreeId(copyTreeId);
	}

	public CreationTree() {
		super();
	}

	public int getCopyTreeId() {
		return copyTreeId;
	}

	public void setCopyTreeId(int copyTreeId) {
		this.copyTreeId = copyTreeId;
	}
}
