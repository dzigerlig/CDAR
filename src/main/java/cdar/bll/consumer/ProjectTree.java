package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;

public class ProjectTree extends BasicEntity {
	private String name;

	public ProjectTree() {
		super();
	}

	public ProjectTree(int id, Date creationDate, Date lastModification,
			String name) {
		super(id, creationDate, lastModification);
		this.name = name;
	}

	public ProjectTree(ProjectTreeDao tree) {
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getName());
	}

	public ProjectTree(int id) {
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
