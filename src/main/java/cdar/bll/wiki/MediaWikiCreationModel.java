package cdar.bll.wiki;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;

public class MediaWikiCreationModel extends Thread {
	private int ktrid;
	private int uid;
	private String title;
	private String templateContent;
	private WikiEntryConcurrentHelper wikiHelper;
	private String wikiConnection;

	public MediaWikiCreationModel(int uid, int ktrid, String title,
			String templateContent, WikiEntryConcurrentHelper wikiHelper) {
		super();
		setKtrid(ktrid);
		setTitle(title);
		setWikiHelper(wikiHelper);
		setTemplateContent(templateContent);
		setUid(uid);
		setWikiConnection();
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWikiHelper(WikiEntryConcurrentHelper wikiHelper) {
		this.wikiHelper = wikiHelper;
	}
	
	public WikiEntryConcurrentHelper getWikiHelper() {
		return wikiHelper;
	}

	public void createNewWikiEntry(String username, String password) {
		Wiki wiki = new Wiki(wikiConnection,"");
		try {
			createEntry(wiki, username, password);
		} catch (Exception e) {
			try {
				// trying again if it is the first time
				createEntry(wiki, username, password);
			} catch (LoginException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void createEntry(Wiki wiki, String username, String password) throws IOException,
			FailedLoginException, LoginException {
		wiki.login(username, password);
		wiki.edit(getTitle(), getTemplateContent(), "");
	}
	
	private void setWikiConnection() {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {			prop.load(resourceStream);
			wikiConnection = prop.getProperty("MEDIAWIKI_CONNECTION");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		try {
			UserManager um = new UserManager();
			User user = um.getUser(getUid());
			createNewWikiEntry(user.getUsername(), user.getPassword());
			getWikiHelper().removeWikiEntry(getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	

}
