package cdar.bll.entity.consumer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

@XmlRootElement
public class ProjectTreeFull extends ProjectTreeSimple {
	private Set<WikiEntry> wikiEntries;
	
	private MediaWikiModel mwm = new MediaWikiModel();
	
	public ProjectTreeFull() { }
	
	public ProjectTreeFull(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownProjectNodeException, UnknownProjectSubnodeException {
		super(treeId);
		fillWikiEntries();
	}
	
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
	
	@XmlElement
	public Set<WikiEntry> getWikiEntries() {
		return wikiEntries;
	}

	public void setWikiEntries(Set<WikiEntry> wikiEntries) {
		this.wikiEntries = wikiEntries;
	}
}
