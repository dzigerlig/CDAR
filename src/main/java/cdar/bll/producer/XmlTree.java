package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.XmlTreeDao;

public class XmlTree extends BasicEntity {
	private int uid;
	private int ktrid;
	private String xmlString;
	
	public XmlTree() {
		super();
	}
	
	public XmlTree(int id, Date creationDate, Date lastModification, int uid, int ktrid, String xmlString) {
		super(id, creationDate, lastModification);
		setUid(uid);
		setKtrid(ktrid);
		setXmlString(xmlString);
	}

	public XmlTree(XmlTreeDao xmlTreeDao) {
		this(xmlTreeDao.getId(), xmlTreeDao.getCreationTime(), xmlTreeDao.getLastModificationTime(), xmlTreeDao.getUid(), xmlTreeDao.getKtrid(), xmlTreeDao.getXmlString());
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
}
