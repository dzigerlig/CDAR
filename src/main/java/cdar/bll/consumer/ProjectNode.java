package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectNodeDao;

public class ProjectNode extends WikiEntity {
	private int nodeStatus;
	private int refProjectTreeId;
	
	public ProjectNode() {
		super();
	}

	public ProjectNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeStatus, int refProjectTreeId) {
		super(id, creationDate, lastModification, title, wikititle);
		this.setNodeStatus(nodeStatus);
		this.setRefProjectTreeId(refProjectTreeId);
	}

	public ProjectNode(KnowledgeProjectNodeDao pnd) {
		this(pnd.getId(), pnd.getCreationTime(), pnd.getLastModificationTime(), pnd.getTitle(), pnd.getWikititle(), pnd.getNodestatus(), pnd.getKnowledgeProjectTree().getId());
	}

	public int getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(int nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public int getRefProjectTreeId() {
		return refProjectTreeId;
	}

	public void setRefProjectTreeId(int refProjectTreeId) {
		this.refProjectTreeId = refProjectTreeId;
	}
	
}
