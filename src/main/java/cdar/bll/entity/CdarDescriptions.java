package cdar.bll.entity;

import java.io.InputStream;
import java.util.Properties;

public class CdarDescriptions {
	private String directoryDescription;
	private String nodeDescription;
	private String subnodeDescription;
	private String wikiUrl;

	public CdarDescriptions() throws Exception {
		getPropertyValue();
	}

	private void getPropertyValue() throws Exception {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {
			prop.load(resourceStream);
			setDirectoryDescription(prop.getProperty("DIRECTORY_DESCRIPTION"));
			setNodeDescription(prop.getProperty("NODE_DESCRIPTION"));
			setSubnodeDescription(prop.getProperty("SUBNODE_DESCRIPTION"));
			setWikiUrl(prop.getProperty("MEDIAWIKI_CONNECTION"), prop.getProperty("MEDIAWIKI_PAGEURL"));
		} catch (Exception ex) {
			throw ex;
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

	public void setWikiUrl(String domain, String page) {
		if (domain.contains("http://")) {
			this.wikiUrl = String.format("%s/%s/", domain, page);
		} else {
			this.wikiUrl = String.format("http://%s/%s/", domain, page);
		}
	}

}
