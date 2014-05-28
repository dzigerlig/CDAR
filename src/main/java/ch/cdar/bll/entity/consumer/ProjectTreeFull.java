package ch.cdar.bll.entity.consumer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class ProjectTreeFull.
 */
@XmlRootElement
public class ProjectTreeFull extends ProjectTreeSimple {
	
	/** The wiki entries. */
	private Set<WikiEntry> wikiEntries;
	
	/** The mwm. */
	private MediaWikiManager mwm = new MediaWikiManager();
	
	/**
	 * Instantiates a new project tree full.
	 */
	public ProjectTreeFull() { }
	
	/**
	 * Instantiates a new project tree full.
	 *
	 * @param treeId the tree id
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public ProjectTreeFull(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownProjectNodeException, UnknownProjectSubnodeException, UnknownTreeException, UnknownUserException {
		super(treeId);
		fillWikiEntries();
	}
	
	/**
	 * Fill wiki entries.
	 *
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 */
	private void fillWikiEntries() throws UnknownProjectNodeException, EntityException, UnknownProjectSubnodeException {
		Set<WikiEntry> wikiEntries = new HashSet<WikiEntry>();
		
		for (ProjectNode projectNode : getProjectNodes()) {
			wikiEntries.add(mwm.getProjectNodeWikiEntry(projectNode.getId()));
		}
		
		for (ProjectSubnode projectSubnode : getProjectSubnodes()) {
			wikiEntries.add(mwm.getKnowledgeProjectSubnodeWikiEntry(projectSubnode.getId()));
		}
		
		setWikiEntries(wikiEntries);
	}
	
	/**
	 * Gets the wiki entries.
	 *
	 * @return the wiki entries
	 */
	@XmlElement
	public Set<WikiEntry> getWikiEntries() {
		return wikiEntries;
	}

	/**
	 * Sets the wiki entries.
	 *
	 * @param wikiEntries the new wiki entries
	 */
	public void setWikiEntries(Set<WikiEntry> wikiEntries) {
		this.wikiEntries = wikiEntries;
	}
}
