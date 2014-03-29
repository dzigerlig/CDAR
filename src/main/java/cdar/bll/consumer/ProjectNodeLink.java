package cdar.bll.consumer;

import java.util.Date;

import cdar.bll.BasicEntity;

public class ProjectNodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int refProjectSubNodeId;
	private int refProjectTreeId;

	public ProjectNodeLink() {
		super();
	}

	public ProjectNodeLink(int id, Date creationDate, Date lastModification,
			int sourceId, int targetId, int refProjectSubNodeId,
			int refProjectTreeId) {
		super(id, creationDate, lastModification);
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.setRefProjectSubNodeId(refProjectSubNodeId);
		this.setRefProjectTreeId(refProjectTreeId);
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

	public int getRefProjectSubNodeId() {
		return refProjectSubNodeId;
	}

	public void setRefProjectSubNodeId(int refProjectSubNodeId) {
		this.refProjectSubNodeId = refProjectSubNodeId;
	}

	public int getRefProjectTreeId() {
		return refProjectTreeId;
	}

	public void setRefProjectTreeId(int refProjectTreeId) {
		this.refProjectTreeId = refProjectTreeId;
	}

}
