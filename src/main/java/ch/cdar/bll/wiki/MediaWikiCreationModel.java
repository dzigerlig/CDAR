package ch.cdar.bll.wiki;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import ch.cdar.bll.entity.User;
import ch.cdar.bll.helpers.WikiEntryConcurrentHelper;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.dal.exceptions.WikiLoginException;
import ch.cdar.dal.repository.wiki.WikiRepository;

/**
 * The Class MediaWikiCreationModel.
 */
public class MediaWikiCreationModel extends Thread {
	/** The user id. */
	private int uid;
	
	/** The title. */
	private String title;
	
	/** The content. */
	private String content;
	
	/** The wiki helper. */
	private WikiEntryConcurrentHelper wikiHelper;
	

	private CountDownLatch subnodeLatch;
	
	
	
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
	
	public MediaWikiCreationModel(int uid, String title, String content, WikiEntryConcurrentHelper wikiHelper, CountDownLatch subnodeLatch) {
		this(uid,title,content,wikiHelper);
		this.subnodeLatch = subnodeLatch;
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
		finally{
			if(subnodeLatch!=null)
			{
				subnodeLatch.countDown();
			}
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
