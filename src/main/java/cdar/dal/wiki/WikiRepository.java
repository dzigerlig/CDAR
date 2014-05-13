package cdar.dal.wiki;

import java.io.InputStream;
import java.util.Properties;

import org.wikipedia.Wiki;

import cdar.dal.exceptions.WikiLoginException;

public class WikiRepository {
	private Wiki wiki = null;
	private String wikiConnection;

	public WikiRepository() {
		setWikiConnection();
		wiki = new Wiki(wikiConnection, "");
	}

	private void setWikiConnection() {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {
			prop.load(resourceStream);
			wikiConnection = prop.getProperty("MEDIAWIKI_CONNECTION");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void tryLogin(String username, String password)
			throws WikiLoginException {
		try {
			wiki.login(username, password);
		} catch (Exception ex) {
			throw new WikiLoginException();
		}
	}
}
