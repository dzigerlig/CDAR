package cdar.bll.entity;

/**
 * Class which contains a whole Tree as a XML String Value
 * @author dani
 *
 */
public class TreeXml extends BasicEntity {
	private int userId;
	private int treeId;
	private String title;
	private String xmlString;
	private boolean isFull;
	
	/**
	 * Default Constructor calling the Constructor of BasicEntity
	 */
	public TreeXml() {
		super();
	}

	/**
	 * 
	 * @return user id of the specified User who created the Object
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId to be set as int Value
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return int Value of the specified Tree
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * 
	 * @param treeId of the specified Tree to be set (int)
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * 
	 * @return String value of the XML String
	 */
	public String getXmlString() {
		return xmlString;
	}

	/**
	 * 
	 * @param xmlString of the exported Tree to be set as a String
	 */
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	/**
	 * 
	 * @return boolean Value (true if the Xml String is a Full Export, false if the Xml String is a Simple Export)
	 */
	public boolean getIsFull() {
		return isFull;
	}

	/**
	 * 
	 * @param isFull boolean Value - true if the XML String represents a Full Export, false if the XML String represents a Simple Export
	 */
	public void setIsFull(boolean isFull) {
		this.isFull = isFull;
	}

	/**
	 * 
	 * @return Title or Name of the specified XML Export
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title or name of the specified XML Export to be set as a String Value
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
