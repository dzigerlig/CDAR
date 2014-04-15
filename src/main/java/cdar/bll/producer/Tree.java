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
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getName());
	}

	public Tree(int id, Date creationDate, Date lastModification,
			String name) {
		super(id, creationDate, lastModification);
		setTitle(name);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}
}
