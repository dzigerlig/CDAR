package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectTreeDao;

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

	public ProjectTree(KnowledgeProjectTreeDao kptd) {
		this(kptd.getId(), kptd.getCreationTime(), kptd.getLastModificationTime(), kptd.getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
