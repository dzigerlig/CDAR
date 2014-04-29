package cdar.bll.wiki;

import cdar.bll.consumer.managers.ProjectNodeManager;
import cdar.bll.producer.managers.NodeManager;
import cdar.bll.producer.managers.SubnodeManager;
import cdar.bll.user.User;
import cdar.bll.user.UserManager;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownUserException;

public class MediaWikiModel {
	private UserManager um = new UserManager();

	public WikiEntry getProjectNodeWikiEntry(int nodeid) throws UnknownProjectNodeException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		return new WikiEntry(pnm.getProjectNode(nodeid));
	}

	public WikiEntry getKnowledgeNodeWikiEntry(int nodeid) throws UnknownNodeException {
		NodeManager nm = new NodeManager();
		return new WikiEntry(nm.getNode(nodeid));
	}

	public WikiEntry getKnowledgeSubnodeWikiEntry(int subnodeid) throws UnknownSubnodeException {
		SubnodeManager sm = new SubnodeManager();
		return new WikiEntry(sm.getSubnode(subnodeid));
	}

	public WikiEntry saveKnowledgeNodeWikiEntry(int userid, WikiEntry wikiEntry) throws UnknownUserException {
		User user = um.getUser(userid);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	public WikiEntry saveKnowledgeSubnodeWikiEntry(int userid, WikiEntry wikiEntry) throws UnknownUserException {
		User user = um.getUser(userid);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

}
