package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeSubNodeDao;

public class SubNode extends WikiEntity {
	private int refNodeId;

	public SubNode() {
		super();
	}

	public SubNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refNodeId) {
		super(id, creationDate, lastModification, title, wikititle);
		this.refNodeId = refNodeId;
	}

	public SubNode(KnowledgeSubNodeDao ksnd) {
		this(ksnd.getId(), ksnd.getCreationTime(), ksnd.getLastModificationTime(),ksnd.getTitle(),ksnd.getWikititle(),ksnd.getKnowledgeNode().getId());
	}

	public int getRefNodeId() {
		return refNodeId;
	}

	public void setRefNodeId(int refNodeId) {
		this.refNodeId = refNodeId;
	}
}
