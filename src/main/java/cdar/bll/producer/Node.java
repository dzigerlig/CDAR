package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.producer.NodeDao;

public class Node extends WikiEntity {
	private int ktrid;
	private int dynamicTreeFlag;
	private int did;
	private String wikititle;

	public Node() {
		super();
	}

	public Node(int id, Date creationDate, Date lastModification, String title,
			String wikititle, int ktrid, int dynamicTreeFlag, int did) {
		super(id, creationDate, lastModification, title, wikititle);
		setKtrid(ktrid);
		setDynamicTreeFlag(dynamicTreeFlag);
		setDid(did);
	}

	public Node(NodeDao nodeDao) {
		this(nodeDao.getId(), nodeDao.getCreationTime(), nodeDao
				.getLastModificationTime(), nodeDao.getTitle(), nodeDao
				.getWikititle(), nodeDao.getKtrid(), nodeDao
				.getDynamicTreeFlag(), nodeDao.getDid());
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public int getDynamicTreeFlag() {
		return dynamicTreeFlag;
	}

	public void setDynamicTreeFlag(int dynamicTreeFlag) {
		this.dynamicTreeFlag = dynamicTreeFlag;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
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
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result
				+ ((getWikiTitle() == null) ? 0 : getWikiTitle().hashCode());	
		result = prime * result + did;
		result = prime * result + dynamicTreeFlag;
		result = prime * result + ktrid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;

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
		if (getTitle() == null) {
			if (other.getTitle() != null)
				return false;
		} else if (!getTitle().equals(other.getTitle()))
			return false;
		if (getWikiTitle() == null) {
			if (other.getWikiTitle() != null)
				return false;
		} else if (!getWikiTitle().equals(other.getWikiTitle()))
			return false;
		else if (did != other.did)
			return false;
		else if (dynamicTreeFlag != other.dynamicTreeFlag)
			return false;
		else if (ktrid != other.ktrid)
			return false;
		return true;
	}

	public String getWikititle() {
		return wikititle;
	}

	public void setWikititle(String wikititle) {
		this.wikititle = wikititle;
	}
}
