package ch.cdar.bll.entity.producer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class TreeFull.
 */
@XmlRootElement
public class TreeFull extends TreeSimple {
	/** The wiki entries. */
	private Set<WikiEntry> wikiEntries;
	
	/** The mwm. */
	private MediaWikiManager mwm = new MediaWikiManager();
	
	/**
	 * Instantiates a new tree full.
	 */
	public TreeFull() {}
	
	/**
	 * Instantiates a new tree full.
	 *
	 * @param treeId the tree id
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public TreeFull(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownSubnodeException, UnknownProjectTreeException {
		super(treeId);
		fillWikiEntries();
	}
	
	/**
	 * Fill wiki entries with the content of the wiki pages.
	 *
	 * @throws EntityException the entity exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	private void fillWikiEntries() throws EntityException, UnknownSubnodeException, UnknownNodeException {
		Set<WikiEntry> wikiEntries = new HashSet<WikiEntry>();
		
		for (Node node : getNodes()) {
			wikiEntries.add(mwm.getKnowledgeNodeWikiEntry(node.getId()));
		}
		
		for (Subnode subnode : getSubnodes()) {
			wikiEntries.add(mwm.getKnowledgeSubnodeWikiEntry(subnode.getId()));
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
