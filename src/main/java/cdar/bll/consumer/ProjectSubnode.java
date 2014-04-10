package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeDao;

public class ProjectSubnode extends WikiEntity {
	private int refProjectNodeId;

	public ProjectSubnode() {
		super();
	}
	
	public ProjectSubnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refProjectNodeId) {
		super(id, creationDate, lastModification, title, wikititle);
		setRefProjectNodeId(refProjectNodeId);
	}

	public ProjectSubnode(ProjectSubnodeDao snd) {
		this(snd.getId(), snd.getCreationTime(), snd.getLastModificationTime(), snd.getTitle(), snd.getWikititle(), snd.getKpnid());
	}

	public int getRefProjectNodeId() {
		return refProjectNodeId;
	}

	public void setRefProjectNodeId(int refProjectNodeId) {
		this.refProjectNodeId = refProjectNodeId;
	}
}
