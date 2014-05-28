package ch.cdar.bll.wiki;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class WikiEntryConcurrentHelper.
 */
public class WikiEntryConcurrentHelper {
	
	/** The wiki list. */
	private static volatile Map<String, String> wikiList = new HashMap<String, String>();

	/**
	 * Checks if is key in map.
	 *
	 * @param key the key
	 * @return true, if is key in map
	 */
	public boolean isKeyInMap(String key) {
		return wikiList.containsKey(key);
	}

	/**
	 * Gets the value.
	 *
	 * @param key the key
	 * @return the value
	 */
	public String getValue(String key) {
		return wikiList.get(key);
	}

	/**
	 * Adds the wiki entry.
	 *
	 * @param wikiTitle the wiki title
	 * @param wikiText the wiki text
	 */
	public synchronized void addWikiEntry(String wikiTitle, String wikiText) {
		wikiList.put(wikiTitle, wikiText);
	}

	/**
	 * Removes the wiki entry.
	 *
	 * @param title the title
	 */
	public synchronized void removeWikiEntry(String title) {
		wikiList.remove(title);
	}
}