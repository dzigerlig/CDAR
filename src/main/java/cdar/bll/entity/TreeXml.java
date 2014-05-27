package cdar.bll.entity;

/**
 * Class which contains a whole Tree as a XML String Value.
 *
 * @author dani
 */
public class TreeXml extends BasicEntity {
	
	/** The user id. */
	private int userId;
	
	/** The tree id. */
	private int treeId;
	
	/** The title. */
	private String title;
	
	/** The xml string. */
	private String xmlString;
	
	/** The is full. */
	private boolean isFull;
	
	/**
	 * Default Constructor calling the Constructor of BasicEntity.
	 */
	public TreeXml() {
		super();
	}

	/**
	 * Gets the user id.
	 *
	 * @return user id of the specified User who created the Object
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId to be set as int Value
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Gets the tree id.
	 *
	 * @return int Value of the specified Tree
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId of the specified Tree to be set (int)
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * Gets the xml string.
	 *
	 * @return String value of the XML String
	 */
	public String getXmlString() {
		return xmlString;
	}

	/**
	 * Sets the xml string.
	 *
	 * @param xmlString of the exported Tree to be set as a String
	 */
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	/**
	 * Gets the checks if is full.
	 *
	 * @return boolean Value (true if the Xml String is a Full Export, false if the Xml String is a Simple Export)
	 */
	public boolean getIsFull() {
		return isFull;
	}

	/**
	 * Sets the checks if is full.
	 *
	 * @param isFull boolean Value - true if the XML String represents a Full Export, false if the XML String represents a Simple Export
	 */
	public void setIsFull(boolean isFull) {
		this.isFull = isFull;
	}

	/**
	 * Gets the title.
	 *
	 * @return Title or Name of the specified XML Export
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title or name of the specified XML Export to be set as a String Value
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
