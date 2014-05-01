package cdar.bll.entity;

import java.util.Date;

public class XmlTree extends BasicEntity {
	private int userId;
	private int treeId;
	private String xmlString;
	
	public XmlTree() {
		super();
	}
	
	public XmlTree(int id, Date creationDate, Date lastModification, int userId, int treeId, String xmlString) {
		super(id, creationDate, lastModification);
		setUserId(userId);
		setTreeId(treeId);
		setXmlString(xmlString);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
}
