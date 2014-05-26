package cdar.bll.entity.consumer;

import cdar.bll.entity.Tree;

public class CreationTree extends Tree {
	private int copyTreeId;

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
