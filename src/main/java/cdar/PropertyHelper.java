package cdar;

import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {
	private final String RESOURCENAME = "cdarconfig.properties";
	private Properties prop;
	private ClassLoader loader;

	public PropertyHelper() {
		setLoader(Thread.currentThread().getContextClassLoader());
		setProp(new Properties());
	}

	public String getProperty(String name) {
		try (InputStream resourceStream = getLoader()
				.getResourceAsStream(RESOURCENAME)) {
			getProp().load(resourceStream);
			return getProp().getProperty(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public ClassLoader getLoader() {
		return loader;
	}

	public void setLoader(ClassLoader loader) {
		this.loader = loader;
	}
}
