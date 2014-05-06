package cdar.bll.entity.consumer;

import java.util.Date;

import cdar.bll.entity.Tree;

public class CreationTree extends Tree {
	private int copyTreeid;

	public CreationTree(int copyTreeid, int id, Date creationDate,
			Date lastModification, String title) {
		super(id, creationDate, lastModification, title);
		setCopyTreeid(copyTreeid);
	}

	public CreationTree() {
		super();
	}

	public int getCopyTreeid() {
		return copyTreeid;
	}

	public void setCopyTreeid(int copyTreeid) {
		this.copyTreeid = copyTreeid;
	}
}
