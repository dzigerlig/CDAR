package cdar.bll.producer;

import java.io.Serializable;
import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.producer.NodeDao;

public class Node extends WikiEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int refTreeId;
	private int dynamicTreeFlag;
	private int did;

	public Node() {
		super();
	}

	public Node(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int refTreeId, int dynamicTreeFlag, int did) {
		super(id, creationDate, lastModification, title, wikititle);
		setRefTreeId(refTreeId);
		setDynamicTreeFlag(dynamicTreeFlag);
		setDid(did);
	}
	
	public Node(NodeDao nodeDao) {
		this(nodeDao.getId(), nodeDao.getCreationTime(),nodeDao.getLastModificationTime(),nodeDao.getTitle(),nodeDao.getWikititle(),nodeDao.getKtrid(),nodeDao.getDynamicTreeFlag(), nodeDao.getDid());
	}

	public int getRefTreeId() {
		return refTreeId;
	}

	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
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
}
