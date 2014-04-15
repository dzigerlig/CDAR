package cdar.bll.wiki;

import cdar.bll.consumer.models.ProjectNodeModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;

public class MediaWikiModel {

	public WikiEntry getProjectNodeWikiEntry(int nodeid) {
		ProjectNodeModel pnm = new ProjectNodeModel();
		return new WikiEntry(pnm.getProjectNode(nodeid));
	}

	public WikiEntry getKnowledgeNodeWikiEntry(int nodeid) {
		NodeModel nm = new NodeModel();
		return new WikiEntry(nm.getNode(nodeid));
	}

	public WikiEntry getKnowledgeSubnodeWikiEntry(int subnodeid) {
		SubnodeModel sm = new SubnodeModel();
		return new WikiEntry(sm.getSubnode(subnodeid));
	}

	public WikiEntry saveKnowledgeNodeWikiEntry(WikiEntry wikiEntry) {
		return wikiEntry.saveEntry();
	}

	public WikiEntry saveKnowledgeSubnodeWikiEntry(WikiEntry wikiEntry) {
		return wikiEntry.saveEntry();
	}

}
