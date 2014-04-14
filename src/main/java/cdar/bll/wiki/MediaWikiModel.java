package cdar.bll.wiki;

import java.io.IOException;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

public class MediaWikiModel extends Thread{	
	private int ktrid; 
	private String title;
	private String templateContent;
	private WikiEntryConcurrentHelper wikiHelper;
	
	public MediaWikiModel(int ktrid, String title, String templateContent, WikiEntryConcurrentHelper wikiHelper) {
		super();
		setKtrid(ktrid);
		setTitle(title);
		setWikiHeper(wikiHelper);
		setTemplateContent(templateContent);
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

	public void setWikiHeper(WikiEntryConcurrentHelper wikiHelper) {
		this.wikiHelper = wikiHelper;
	}

	public void createNewWikiEntry() {
		Wiki wiki = new Wiki();
		try {
			createEntry(wiki);
		} catch (Exception e) {
			try {
				//trying again if it is the first time
				createEntry(wiki);
			} catch (LoginException | IOException e1) {
				e1.printStackTrace();
			}
		}	
	}

	private void createEntry(Wiki wiki) throws IOException,
			FailedLoginException, LoginException {
		wiki.login("admin", "password");
		if (getTemplateContent() == null) {
			wiki.edit(this.title, getTemplateContent(), "== CDAR ==");
		} else {
			wiki.edit(this.title, getTemplateContent(), "");
		}
	}

	public void run() {
		createNewWikiEntry();
		this.wikiHelper.removeWikiEntry(getTitle());
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
}
