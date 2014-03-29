package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeLinkDao;

public class NodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int refSubNodeId;
	private int refTreeId;
	
	
	public NodeLink() {
		super();
	}
	
	
	public NodeLink(int id, Date creationDate, Date lastModification,
			int sourceId, int targetId) {
		super(id, creationDate, lastModification);
		this.sourceId = sourceId;
		this.targetId = targetId;
	}
	
	public NodeLink(KnowledgeNodeLinkDao klcd) {
		this(klcd.getId(), klcd.getCreationTime(),klcd.getLastModificationTime(),klcd.getKnowledgeSourceNode().getId(),klcd.getKnowledgeTargetNode().getId());
	}

	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}


	public int getRefSubNodeId() {
		return refSubNodeId;
	}


	public void setRefSubNodeId(int refSubNodeId) {
		this.refSubNodeId = refSubNodeId;
	}


	public int getRefTreeId() {
		return refTreeId;
	}


	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
	}
	
}
