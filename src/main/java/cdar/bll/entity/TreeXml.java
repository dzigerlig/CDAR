package cdar.bll.entity;

import java.util.Date;

public class TreeXml extends BasicEntity {
	private int userId;
	private int treeId;
	private String title;
	private String xmlString;
	private boolean isFull;
	
	public TreeXml() {
		super();
	}
	
	public TreeXml(int id, Date creationDate, Date lastModification, int userId, int treeId, String xmlString) {
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

	public boolean getIsFull() {
		return isFull;
	}

	public void setIsFull(boolean isFull) {
		this.isFull = isFull;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
