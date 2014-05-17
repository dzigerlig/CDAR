package cdar.dal.wiki;

import java.util.HashMap;
import java.util.Map;

import org.wikipedia.Wiki;

import cdar.PropertyHelper;
import cdar.dal.exceptions.WikiLoginException;

public class WikiRepositoryq {
	private static Map<String, Wiki> connectionMap = new HashMap<String, Wiki>();

	public Wiki getConnection(String username, String password) throws WikiLoginException {
		if (connectionMap.containsKey(username + password)) {
			return connectionMap.get(username + password);
		} else {
			PropertyHelper property = new PropertyHelper();
			Wiki wiki = new Wiki(
					property.getProperty("MEDIAWIKI_CONNECTION"),"");
			try {
				wiki.login(username, password);
			} catch (Exception ex) {
				throw new WikiLoginException();
			}
			connectionMap.put(username + password, wiki);
			return wiki;
		}
	}
}
