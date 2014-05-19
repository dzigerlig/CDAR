package cdar.bll.entity;

import java.util.Date;

public class Directory extends BasicEntity implements Comparable<Directory> {
	private int parentId;
	private int treeId;
	private String title;


	public Directory() {
		super();
	}

	public Directory(int id, Date creationTime, Date lastModificationTime,
			int parentId, int treeId, String title) {
		super(id, creationTime, lastModificationTime);
		setParentId(parentId);
		setTreeId(treeId);
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

	@Override
	public int compareTo(Directory o) {
		return Integer.compare(this.parentId, o.parentId);
	}
}
