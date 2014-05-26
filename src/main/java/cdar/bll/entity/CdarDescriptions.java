package cdar.bll.entity;

import cdar.dal.helpers.PropertyHelper;

/**
 * Class containing values out of the property file, used to transfer the configured value to the client application
 * @author dzigerli
 * @author mtinner
 *
 */
public class CdarDescriptions {
	private String directoryDescription;
	private String nodeDescription;
	private String subnodeDescription;
	private String wikiUrl;
	private String expandedLevel;

	/**
	 * Default Constructor which immediately calls getPropertyValues()
	 * @throws Exception 
	 */
	public CdarDescriptions() throws Exception {
		getPropertyValues();
	}

	/**
	 * Loads all values out of the property file and stores its values into the member variables
	 * @throws Exception
	 */
	private void getPropertyValues() throws Exception {
		PropertyHelper propertyHelper = new PropertyHelper();
		setDirectoryDescription(propertyHelper.getProperty("DIRECTORY_DESCRIPTION"));
		setNodeDescription(propertyHelper.getProperty("NODE_DESCRIPTION"));
		setSubnodeDescription(propertyHelper.getProperty("SUBNODE_DESCRIPTION"));
		setWikiUrl(propertyHelper.getProperty("MEDIAWIKI_CONNECTION"), propertyHelper.getProperty("MEDIAWIKI_PAGEURL"));
		setExpandedLevel(propertyHelper.getProperty("EXPANDING_LEVEL"));
	}

	/**
	 * sets the wikiUrl depending on the passed parameters (domain and page)
	 * @param domain
	 * @param page
	 */
	private void setWikiUrl(String domain, String page) {
		if (domain.contains("http://")) {
			this.wikiUrl = String.format("%s/%s/", domain, page);
		} else {
			this.wikiUrl = String.format("http://%s/%s/", domain, page);
		}
	}
	
	/**
	 * 
	 * @return current directory description as String
	 */
	public String getDirectoryDescription() {
		return directoryDescription;
	}

	/**
	 * 
	 * @param directoryDescription description for a directory to set (String)
	 */
	public void setDirectoryDescription(String directoryDescription) {
		this.directoryDescription = directoryDescription;
	}

	/**
	 * 
	 * @return current node description as String
	 */
	public String getNodeDescription() {
		return nodeDescription;
	}

	/**
	 * 
	 * @param nodeDescription sets description of node (String)
	 */
	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	/**
	 * 
	 * @return current subnode description as String
	 */
	public String getSubnodeDescription() {
		return subnodeDescription;
	}

	/**
	 * 
	 * @param subnodeDescription description for subnode to set (String)
	 */
	public void setSubnodeDescription(String subnodeDescription) {
		this.subnodeDescription = subnodeDescription;
	}

	/**
	 * 
	 * @return current wiki url as String
	 */
	public String getWikiUrl() {
		return wikiUrl;
	}

	/**
	 * 
	 * @return current expanded level as String
	 */
	public String getExpandedLevel() {
		return expandedLevel;
	}

	/**
	 * 
	 * @param expandedLevel value of expanded level (String)
	 */
	public void setExpandedLevel(String expandedLevel) {
		this.expandedLevel = expandedLevel;
	}
}
