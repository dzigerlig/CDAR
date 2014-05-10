package cdar.bll.entity.producer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class TreeFull extends TreeSimple {
	private Set<WikiEntry> wikiEntries;
	
	private MediaWikiModel mwm = new MediaWikiModel();
	
	public TreeFull() {}
	
	public TreeFull(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownSubnodeException {
		super(treeId);
		fillWikiEntries();
	}
	
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
	
	@XmlElement
	public Set<WikiEntry> getWikiEntries() {
		return wikiEntries;
	}

	public void setWikiEntries(Set<WikiEntry> wikiEntries) {
		this.wikiEntries = wikiEntries;
	}
}
