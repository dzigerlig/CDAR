package cdar.bll.producer;

import java.util.List;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;

public class KnowledgeNode extends WikiEntity {
	private List<SubNode> knowledgeSubNodes;
	
	public KnowledgeNode(KnowledgeNodeDao knd) {
		super(knd.getId(), knd.getCreationTime(), knd.getLastModificationTime(), knd.getTitle(), knd.getWikititle());
	}

	public List<SubNode> getKnowledgeSubNodes() {
		return knowledgeSubNodes;
	}
	
	public void setKnowledgeSubNodes(List<SubNode> knowledgeSubNodes) {
		this.knowledgeSubNodes = knowledgeSubNodes;
	}
}
