package cdar.bll.entity;

import java.util.Date;

public class Tree extends BasicEntity {

	private String title;
	private int userId;
	
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
