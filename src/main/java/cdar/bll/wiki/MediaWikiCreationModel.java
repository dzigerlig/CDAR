/*
 * 
 */
package cdar.bll.wiki;

import java.io.IOException;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.dal.exceptions.WikiLoginException;
import cdar.dal.wiki.WikiRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class MediaWikiCreationModel.
 */
public class MediaWikiCreationModel extends Thread {
	
	/** The uid. */
	private int uid;
	
	/** The title. */
	private String title;
	
	/** The content. */
	private String content;
	
	/** The wiki helper. */
	private WikiEntryConcurrentHelper wikiHelper;

	/**
	 * Instantiates a new media wiki creation model.
	 *
	 * @param uid the uid
	 * @param title the title
	 * @param content the content
	 * @param wikiHelper the wiki helper
	 */
	public MediaWikiCreationModel(int uid, String title, String content, WikiEntryConcurrentHelper wikiHelper) {
		super();
		setTitle(title);
		setWikiHelper(wikiHelper);
		setContent(content);
		setUid(uid);
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the wiki helper.
	 *
	 * @param wikiHelper the new wiki helper
	 */
	public void setWikiHelper(WikiEntryConcurrentHelper wikiHelper) {
		this.wikiHelper = wikiHelper;
	}

	/**
	 * Gets the wiki helper.
	 *
	 * @return the wiki helper
	 */
	public WikiEntryConcurrentHelper getWikiHelper() {
		return wikiHelper;
	}

	/**
	 * Creates the new wiki entry.
	 *
	 * @param username the username
	 * @param password the password
	 * @throws WikiLoginException the wiki login exception
	 */
	private void createNewWikiEntry(String username, String password) throws WikiLoginException {
		WikiRepository wikiConnection = new WikiRepository();
		Wiki wiki = wikiConnection.getConnection(username, password);
		try {
			createEntry(wiki);
		} catch (Exception e) {
			try {
				// trying again if it is the first time
				createEntry(wiki);
			} catch (LoginException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Creates the entry.
	 *
	 * @param wiki the wiki
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FailedLoginException the failed login exception
	 * @throws LoginException the login exception
	 */
	private void createEntry(Wiki wiki)
			throws IOException, FailedLoginException, LoginException {
		wiki.edit(getTitle(), getContent(), "");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
}
