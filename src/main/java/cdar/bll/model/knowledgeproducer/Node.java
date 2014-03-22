package cdar.bll.model.knowledgeproducer;

import java.io.Serializable;
import java.util.Date;

import cdar.bll.model.WikiEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;

public class Node extends WikiEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int refTreeId;
	private int status;

	public Node() {
		super();
	}

	public Node(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refTreeId, int status) {
		super(id, creationDate, lastModification, title, wikititle);
		this.refTreeId = refTreeId;
		this.status = status;
	}
	
	public Node(KnowledgeNodeDao knd) {
		this(knd.getId(), knd.getCreationTime(),knd.getLastModificationTime(),knd.getTitle(),knd.getWikititle(),knd.getKnowledgeTree().getId(),0);
	}

	public int getRefTreeId() {
		return refTreeId;
	}

	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	/*
	 * private List<KnowledgeSubNode> knowledgeSubNodes;
	 * 
	 * 
	 * public List<KnowledgeSubNode> getKnowledgeSubNodes() { return
	 * knowledgeSubNodes; }
	 * 
	 * public void setKnowledgeSubNodes(List<KnowledgeSubNode>
	 * knowledgeSubNodes) { this.knowledgeSubNodes = knowledgeSubNodes; }
	 */
}
