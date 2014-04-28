package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;

public class ProjectTree extends BasicEntity {
	private String title;
	private int uid;

	public ProjectTree() {
		super();
	}

	public ProjectTree(int id, Date creationDate, Date lastModification,
			String title) {
		super(id, creationDate, lastModification);
		setTitle(title);
	}

	public ProjectTree(int id) {
		setId(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
