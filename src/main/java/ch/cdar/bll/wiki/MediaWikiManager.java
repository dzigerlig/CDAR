package ch.cdar.bll.wiki;


import java.util.concurrent.CountDownLatch;

import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.helpers.WikiEntryConcurrentHelper;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.bll.manager.consumer.ProjectNodeManager;
import ch.cdar.bll.manager.consumer.ProjectSubnodeManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class MediaWikiManager.
 */
public class MediaWikiManager {
	/** The User Manager. */
	private UserManager um = new UserManager();

	/**
	 * Gets the project node wiki entry.
	 *
	 * @param nodeId the node id
	 * @return the project node wiki entry
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry getProjectNodeWikiEntry(int nodeId) throws UnknownProjectNodeException, EntityException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		return new WikiEntry(pnm.getProjectNode(nodeId));
	}

	/**
	 * Gets the knowledge node wiki entry.
	 *
	 * @param nodeId the node id
	 * @return the knowledge node wiki entry
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry getKnowledgeNodeWikiEntry(int nodeId) throws UnknownNodeException, EntityException {
		NodeManager nm = new NodeManager();
		return new WikiEntry(nm.getNode(nodeId));
	}

	/**
	 * Gets the knowledge subnode wiki entry.
	 *
	 * @param subnodeId the subnode id
	 * @return the knowledge subnode wiki entry
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry getKnowledgeSubnodeWikiEntry(int subnodeId) throws UnknownSubnodeException, EntityException {
		SubnodeManager sm = new SubnodeManager();
		return new WikiEntry(sm.getSubnode(subnodeId));
	}

	/**
	 * Save knowledge node wiki entry.
	 *
	 * @param userId the user id
	 * @param wikiEntry the wiki entry
	 * @return the wiki entry
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry saveKnowledgeNodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	/**
	 * Save knowledge subnode wiki entry.
	 *
	 * @param userId the user id
	 * @param wikiEntry the wiki entry
	 * @return the wiki entry
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry saveKnowledgeSubnodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	/**
	 * Save project node wiki entry.
	 *
	 * @param userId the user id
	 * @param wikiEntry the wiki entry
	 * @return the wiki entry
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry saveProjectNodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}

	/**
	 * Gets the knowledge project subnode wiki entry.
	 *
	 * @param subnodeId the subnode id
	 * @return the knowledge project subnode wiki entry
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry getKnowledgeProjectSubnodeWikiEntry(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		return new WikiEntry(psm.getProjectSubnode(subnodeId));
	}

	/**
	 * Save knowledge project subnode wiki entry.
	 *
	 * @param userId the user id
	 * @param wikiEntry the wiki entry
	 * @return the wiki entry
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public WikiEntry saveKnowledgeProjectSubnodeWikiEntry(int userId, WikiEntry wikiEntry) throws UnknownUserException, EntityException {
		User user = um.getUser(userId);
		return wikiEntry.saveEntry(user.getUsername(), user.getPassword());
	}
	
	/**
	 * Creates the wiki entry.
	 *
	 * @param userId the user id
	 * @param title the title
	 * @param content the content
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public void createWikiEntry(int userId, String title, String content) throws UnknownUserException, EntityException {
		WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();
		wikiHelper.addWikiEntry(title, content);
		MediaWikiCreationModel mwm = new MediaWikiCreationModel(userId, title, content, wikiHelper);
		mwm.start();
	}
	
	/**
	 * Creates the wiki entry triggered by thread.
	 *
	 * @param userId the user id
	 * @param title the title
	 * @param content the content
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws InterruptedException 
	 */
	public void createWikiEntry(int userId, String title, String content, CountDownLatch subnodeLatch) throws UnknownUserException, EntityException, InterruptedException {
		WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();
		wikiHelper.addWikiEntry(title, content);
		MediaWikiCreationModel mwm = new MediaWikiCreationModel(userId, title, content, wikiHelper, subnodeLatch);
		mwm.start();
	}
}
