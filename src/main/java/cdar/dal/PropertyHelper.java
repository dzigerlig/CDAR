package cdar.dal;

import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {
	private final String RESOURCENAME = "cdarconfig.properties";
	private Properties prop;
	private ClassLoader loader;

	public PropertyHelper() {
		super();
		loader = Thread.currentThread().getContextClassLoader();
		prop = new Properties();
	}

	public String getProperty(String name) {
		try (InputStream resourceStream = loader
				.getResourceAsStream(RESOURCENAME)) {
			prop.load(resourceStream);
			return prop.getProperty(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
