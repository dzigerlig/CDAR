package cdar.dal.wiki;

import org.wikipedia.Wiki;

import cdar.dal.exceptions.WikiLoginException;

public class WikiRepository {
	private Wiki wiki = null;
	
	public WikiRepository() {
		wiki = new Wiki();
	}
	
	public void tryLogin(String username, String password) throws WikiLoginException {
		try {
			wiki.login(username, password);
		} catch (Exception ex) {
			throw new WikiLoginException();
		}
	}
}
