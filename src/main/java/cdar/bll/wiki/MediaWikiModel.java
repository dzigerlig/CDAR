package cdar.bll.wiki;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownUserException;

public class MediaWikiModel {
	private UserManager um = new UserManager();

	public WikiEntry getProjectNodeWikiEntry(int nodeId) throws UnknownProjectNodeException, EntityException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		return new WikiEntry(pnm.getProjectNode(nodeId));
	}

	public WikiEntry getKnowledgeNodeWikiEntry(int nodeId) throws UnknownNodeException, EntityException {
		NodeManager nm = new NodeManager();
		return new WikiEntry(nm.getNode(nodeId));
	}

	public WikiEntry getKnowledgeSubnodeWikiEntry(int subnodeId) throws UnknownSubnodeException, EntityException {
		SubnodeManager sm = new SubnodeManager();
		return new WikiEntry(sm.getSubnode(subnodeId));
	}

	public WikiEntry saveKnowledgeNodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	public WikiEntry saveKnowledgeSubnodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	public WikiEntry saveProjectNodeWikiEntry(int userid, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userid);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	public WikiEntry getKnowledgeProjectSubnodeWikiEntry(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		return new WikiEntry(psm.getProjectSubnode(subnodeId));
	}

	public WikiEntry saveKnowledgeProjectSubnodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}
}
