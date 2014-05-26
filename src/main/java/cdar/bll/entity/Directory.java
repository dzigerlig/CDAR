package cdar.bll.entity;

public class Directory extends BasicEntity implements Comparable<Directory> {
	private int parentId;
	private int treeId;
	private String title;


	public Directory() {
		super();
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
