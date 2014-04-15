package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class Tree extends BasicEntity {

	private String title;
	
	public Tree() {
		super();
	}

	public Tree(TreeDao tree) {
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getTitle());
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
}
