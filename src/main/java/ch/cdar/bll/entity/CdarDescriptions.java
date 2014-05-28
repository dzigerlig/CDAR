package ch.cdar.bll.entity;

import ch.cdar.dal.helpers.PropertyHelper;

/**
 * Class containing values out of the property file, used to transfer the configured value to the client application.
 *
 * @author dzigerli
 * @author mtinner
 */
public class CdarDescriptions {
	
	/** The directory description. */
	private String directoryDescription;
	
	/** The node description. */
	private String nodeDescription;
	
	/** The subnode description. */
	private String subnodeDescription;
	
	/** The wiki url. */
	private String wikiUrl;
	
	/** The expanded level. */
	private String expandedLevel;

	/**
	 * Default Constructor which calls getPropertyValues().
	 *
	 * @throws Exception the exception
	 */
	public CdarDescriptions() throws Exception {
		getPropertyValues();
	}

	/**
	 * Loads all values out of the property file and stores its values into the member variables.
	 *
	 * @return the property values
	 * @throws Exception the exception
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
	 * sets the wikiUrl depending on the passed parameters (domain and page).
	 *
	 * @param domain the domain
	 * @param page the page
	 */
	private void setWikiUrl(String domain, String page) {
		if (domain.contains("http://")) {
			this.wikiUrl = String.format("%s/%s/", domain, page);
		} else {
			this.wikiUrl = String.format("http://%s/%s/", domain, page);
		}
	}
	
	/**
	 * Gets the directory description.
	 *
	 * @return current directory description as String
	 */
	public String getDirectoryDescription() {
		return directoryDescription;
	}

	/**
	 * Sets the directory description.
	 *
	 * @param directoryDescription description for a directory to set (String)
	 */
	public void setDirectoryDescription(String directoryDescription) {
		this.directoryDescription = directoryDescription;
	}

	/**
	 * Gets the node description.
	 *
	 * @return current node description as String
	 */
	public String getNodeDescription() {
		return nodeDescription;
	}

	/**
	 * Sets the node description.
	 *
	 * @param nodeDescription sets description of node (String)
	 */
	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	/**
	 * Gets the subnode description.
	 *
	 * @return current subnode description as String
	 */
	public String getSubnodeDescription() {
		return subnodeDescription;
	}

	/**
	 * Sets the subnode description.
	 *
	 * @param subnodeDescription description for subnode to set (String)
	 */
	public void setSubnodeDescription(String subnodeDescription) {
		this.subnodeDescription = subnodeDescription;
	}

	/**
	 * Gets the wiki url.
	 *
	 * @return current wiki url as String
	 */
	public String getWikiUrl() {
		return wikiUrl;
	}

	/**
	 * Gets the expanded level.
	 *
	 * @return current expanded level as String
	 */
	public String getExpandedLevel() {
		return expandedLevel;
	}

	/**
	 * Sets the expanded level.
	 *
	 * @param expandedLevel value of expanded level (String)
	 */
	public void setExpandedLevel(String expandedLevel) {
		this.expandedLevel = expandedLevel;
	}
}
