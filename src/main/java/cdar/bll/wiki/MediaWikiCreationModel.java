package cdar.bll.wiki;

import java.io.IOException;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.dal.exceptions.WikiLoginException;
import cdar.dal.wiki.WikiRepository;

public class MediaWikiCreationModel extends Thread {
	private int uid;
	private String title;
	private String content;
	
	private WikiEntryConcurrentHelper wikiHelper;

	public MediaWikiCreationModel(int uid, String title, String content, WikiEntryConcurrentHelper wikiHelper) {
		super();
		setTitle(title);
		setWikiHelper(wikiHelper);
		setContent(content);
		setUid(uid);
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

	private void createEntry(Wiki wiki)
			throws IOException, FailedLoginException, LoginException {
		wiki.edit(getTitle(), getContent(), "");
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
