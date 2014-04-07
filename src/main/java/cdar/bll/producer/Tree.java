package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class Tree extends BasicEntity {

	private String name;

	public Tree(TreeDao tree) {
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getName());
	}

	public Tree(int id, Date creationDate, Date lastModification,
			String name) {
		super(id, creationDate, lastModification);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
