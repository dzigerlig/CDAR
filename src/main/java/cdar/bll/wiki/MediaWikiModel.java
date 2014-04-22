package cdar.bll.wiki;

import cdar.bll.consumer.models.ProjectNodeModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.user.User;
import cdar.bll.user.UserModel;
import cdar.dal.persistence.jdbc.user.UserDao;

public class MediaWikiModel {
	private UserModel um = new UserModel();

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

	public WikiEntry saveKnowledgeNodeWikiEntry(int userid, WikiEntry wikiEntry) {
		User user = um.getUser(userid);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	public WikiEntry saveKnowledgeSubnodeWikiEntry(int userid, WikiEntry wikiEntry) {
		User user = um.getUser(userid);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

}
