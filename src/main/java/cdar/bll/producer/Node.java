package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.producer.NodeDao;

public class Node extends WikiEntity {
	private int ktrid;
	private int dynamicTreeFlag;
	private int did;

	public Node() {
		super();
	}

	public Node(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int ktrid, int dynamicTreeFlag, int did) {
		super(id, creationDate, lastModification, title, wikititle);
		setKtrid(ktrid);
		setDynamicTreeFlag(dynamicTreeFlag);
		setDid(did);
	}
	
	public Node(NodeDao nodeDao) {
		this(nodeDao.getId(), nodeDao.getCreationTime(),nodeDao.getLastModificationTime(),nodeDao.getTitle(),nodeDao.getWikititle(),nodeDao.getKtrid(),nodeDao.getDynamicTreeFlag(), nodeDao.getDid());
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
}
