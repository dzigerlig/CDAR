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
		setKtrid(treeid);
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

	public void setKsnid(int ksnid) {
		this.ksnid = ksnid;
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
		result = prime * result + getId();
		result = prime
				* result
				+ ((getLastModificationTime() == null) ? 0 : getLastModificationTime()
						.hashCode());		
		result = prime * result + ksnid;
		result = prime * result + ktrid;
		result = prime * result + sourceId;
		result = prime * result + targetId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NodeLink))
			return false;
		NodeLink other = (NodeLink) obj;
		
		
		if (getCreationTime() == null) {
			if (other.getCreationTime() != null)
				return false;
		} else if (!getCreationTime().equals(other.getCreationTime()))
			return false;
		if (getId() != other.getId())
			return false;
		if (getLastModificationTime() == null) {
			if (other.getLastModificationTime() != null)
				return false;
		} else if (!getLastModificationTime().equals(
				other.getLastModificationTime()))
			return false;
		if (ksnid != other.ksnid)
			return false;
		if (ktrid != other.ktrid)
			return false;
		if (sourceId != other.sourceId)
			return false;
		if (targetId != other.targetId)
			return false;
		return true;
	}	
}

