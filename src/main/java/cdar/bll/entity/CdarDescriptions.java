package cdar.bll.entity;

import java.io.InputStream;
import java.util.Properties;

public class CdarDescriptions {
	private String nodeDescription;
	private String subnodeDescription;

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
			setNodeDescription(prop.getProperty("NODE_DESCRIPTION"));
			setSubnodeDescription(prop.getProperty("SUBNODE_DESCRIPTION"));
		} catch (Exception ex) {
			throw ex;
		}
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
}
