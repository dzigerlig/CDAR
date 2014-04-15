package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;

public class ProjectTree extends BasicEntity {
	private String title;

	public ProjectTree() {
		super();
	}

	public ProjectTree(int id, Date creationDate, Date lastModification,
			String name) {
		super(id, creationDate, lastModification);
		setName(name);
	}

	public ProjectTree(ProjectTreeDao tree) {
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getName());
	}

	public ProjectTree(int id) {
		setId(id);
	}

	public String getTitle() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}
}
