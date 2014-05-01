package cdar.bll.entity;

import java.util.Date;

public class Directory extends BasicEntity {
	private int parentId;
	private int treeId;
	private String title;


	public Directory() {
		super();
	}

	public Directory(int id, Date creationTime, Date lastModificationTime,
			int parentid,int ktrid, String title) {
		super(id, creationTime, lastModificationTime);
		setParentId(parentid);
		setTreeId(ktrid);
		setTitle(title);
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
