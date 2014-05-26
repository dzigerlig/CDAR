package cdar.bll.entity;

import cdar.dal.helpers.PropertyHelper;

public class CdarDescriptions {
	private String directoryDescription;
	private String nodeDescription;
	private String subnodeDescription;
	private String wikiUrl;
	private String expandedLevel;

	public CdarDescriptions() throws Exception {
		getPropertyValue();
	}

	private void getPropertyValue() throws Exception {
		PropertyHelper propertyHelper = new PropertyHelper();
		setDirectoryDescription(propertyHelper.getProperty("DIRECTORY_DESCRIPTION"));
		setNodeDescription(propertyHelper.getProperty("NODE_DESCRIPTION"));
		setSubnodeDescription(propertyHelper.getProperty("SUBNODE_DESCRIPTION"));
		setWikiUrl(propertyHelper.getProperty("MEDIAWIKI_CONNECTION"), propertyHelper.getProperty("MEDIAWIKI_PAGEURL"));
		setExpandedLevel(propertyHelper.getProperty("EXPANDING_LEVEL"));
	}

	private void setWikiUrl(String domain, String page) {
		if (domain.contains("http://")) {
			this.wikiUrl = String.format("%s/%s/", domain, page);
		} else {
			this.wikiUrl = String.format("http://%s/%s/", domain, page);
		}
	}
	
	public String getDirectoryDescription() {
		return directoryDescription;
	}

	public void setDirectoryDescription(String directoryDescription) {
		this.directoryDescription = directoryDescription;
	}

	public String getNodeDescription() {
		return nodeDescription;
	}

	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	public String getSubnodeDescription() {
		return subnodeDescription;
	}

	public void setSubnodeDescription(String subnodeDescription) {
		this.subnodeDescription = subnodeDescription;
	}

	public String getWikiUrl() {
		return wikiUrl;
	}


	public String getExpandedLevel() {
		return expandedLevel;
	}

	public void setExpandedLevel(String expandedLevel) {
		this.expandedLevel = expandedLevel;
	}
}
