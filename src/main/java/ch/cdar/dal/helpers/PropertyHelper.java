package ch.cdar.dal.helpers;

import java.io.InputStream;
import java.util.Properties;

/**
 * The Class PropertyHelper.
 */
public class PropertyHelper {
	/** The resourcename. */
	private final String RESOURCENAME = "cdarconfig.properties";
	
	/** The properties */
	private Properties prop;
	
	/** The loader. */
	private ClassLoader loader;

	/**
	 * Instantiates a new property helper.
	 */
	public PropertyHelper() {
		setLoader(Thread.currentThread().getContextClassLoader());
		setProp(new Properties());
	}

	/**
	 * Gets the property.
	 *
	 * @param name the name
	 * @return the property
	 */
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

	/**
	 * Gets the prop.
	 *
	 * @return the prop
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * Sets the prop.
	 *
	 * @param prop the new prop
	 */
	public void setProp(Properties prop) {
		this.prop = prop;
	}

	/**
	 * Gets the loader.
	 *
	 * @return the loader
	 */
	public ClassLoader getLoader() {
		return loader;
	}

	/**
	 * Sets the loader.
	 *
	 * @param loader the new loader
	 */
	public void setLoader(ClassLoader loader) {
		this.loader = loader;
	}
}
