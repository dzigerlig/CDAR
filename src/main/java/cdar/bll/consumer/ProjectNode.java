package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;

public class ProjectNode extends WikiEntity {
	private int nodeStatus;
	private int refProjectTreeId;
	
	public ProjectNode() {
		super();
	}

	public ProjectNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int nodeStatus, int refProjectTreeId) {
		super(id, creationDate, lastModification, title, wikititle);
		setNodeStatus(nodeStatus);
		setRefProjectTreeId(refProjectTreeId);
	}

	public ProjectNode(ProjectNodeDao projectNodeDao) {
		this(projectNodeDao.getId(), projectNodeDao.getCreationTime(), projectNodeDao.getLastModificationTime(), projectNodeDao.getTitle(), projectNodeDao.getWikititle(), projectNodeDao.getNodestatus(), projectNodeDao.getKptid());
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
