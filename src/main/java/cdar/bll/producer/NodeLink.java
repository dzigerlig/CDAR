package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;

public class NodeLink extends BasicEntity {
	private int sourceId;
	private int targetId;
	private int ksnid;
	private int ktrid;
	
	public NodeLink() {
		super();
	}
	
	public NodeLink(int id, Date creationDate, Date lastModification,
			int sourceId, int targetId, int ksnid, int treeid) {
		super(id, creationDate, lastModification);
		setSourceId(sourceId);
		setTargetId(targetId);
		setKsnid(ksnid);
		//setTargetId(treeid);
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

	public int getKsnid() {
		return ksnid;
	}

	public void setKsnid(int knid) {
		this.ksnid = knid;
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}
}
