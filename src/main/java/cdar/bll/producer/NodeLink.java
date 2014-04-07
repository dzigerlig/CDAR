package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeLinkDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;

public class NodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int refSubNodeId;
	private int refTreeId;
	
	
	public NodeLink() {
		super();
	}
	
	
	public NodeLink(int id, Date creationDate, Date lastModification,
			int sourceId, int targetId, int subnodeid, int treeid) {
		super(id, creationDate, lastModification);
		this.sourceId = sourceId;
		this.targetId = targetId;
	}
	
	public NodeLink(NodeLinkDao nld) {
		this(nld.getId(), nld.getCreationTime(), nld.getLastModificationTime(), nld.getSourceid(), nld.getTargetid(), nld.getKsnid(), nld.getKtrid());
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
