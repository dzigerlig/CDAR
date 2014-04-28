package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;

public class Tree extends BasicEntity {

	private String title;
	private int uid;
	
	public Tree() {
		super();
	}

	public Tree(int id, Date creationDate, Date lastModification,
			String title) {
		super(id, creationDate, lastModification);
		setTitle(title);
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
