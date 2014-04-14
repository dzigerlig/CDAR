package cdar.bll.wiki;

import java.util.HashMap;
import java.util.Map;

public class WikiEntryConcurrentHelper {
	private static volatile Map<String, String> wikiList = new HashMap<String, String>();

	
	public synchronized void addWikiEntry(String wikiTitle, String wikiText) {
		wikiList.put(wikiTitle, wikiText);
	}

	public synchronized void removeWikiEntry(String title) {
		wikiList.remove(title);
	}
}
