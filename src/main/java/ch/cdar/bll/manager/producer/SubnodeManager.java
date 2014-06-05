package ch.cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.helpers.PropertyHelper;
import ch.cdar.dal.producer.NodeRepository;
import ch.cdar.dal.producer.SubnodeRepository;
import ch.cdar.dal.user.UserRepository;

/**
 * The Class SubnodeManager.
 */
public class SubnodeManager {

	/** The Subnode Repository. */
	private SubnodeRepository sr = new SubnodeRepository();

	/**
	 * Adds the subnode.
	 * 
	 * @param uid
	 *            the uid
	 * @param treeId
	 *            the tree id
	 * @param subnode
	 *            the subnode
	 * @return the subnode
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws CreationException
	 *             the creation exception
	 * @throws UnknownUserException
	 *             the unknown user exception
	 * @throws UnknownTreeException
	 *             the unknown tree exception
	 * @throws InterruptedException 
	 */
	public Subnode addSubnode(int uid, int treeId, Subnode subnode,
			String templateContent, CountDownLatch subnodeLatch) throws EntityException,
			UnknownNodeException, CreationException, UnknownUserException,
			UnknownTreeException, InterruptedException {
		boolean createSubnode = true;
		if (subnode.getWikititle() != null) {
			createSubnode = false;
		}
		if (subnode.getPosition()==0) {
			subnode.setPosition(getNextSubnodePosition(subnode.getNodeId()));
		}
		subnode = sr.createSubnode(subnode);

		if (createSubnode) {
			TemplateManager tm = new TemplateManager();
			if (templateContent == null) {
				templateContent = tm.getDefaultSubnodeTemplateText(treeId);

				if (templateContent == null) {
					PropertyHelper propertyHelper = new PropertyHelper();
					templateContent = String.format("== %S ==",
							propertyHelper.getProperty("CDAR_SUBNODE_DESCRIPTION"));
				}
			}

			MediaWikiManager mwm = new MediaWikiManager();
			if (subnodeLatch==null) {
				mwm.createWikiEntry(uid, subnode.getWikititle(), templateContent);			
			} else {
				mwm.createWikiEntry(uid, subnode.getWikititle(), templateContent, subnodeLatch);
			}
		}

		return subnode;
	}

	/**
	 * Gets the next subnode position.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the next subnode position
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	private int getNextSubnodePosition(int nodeId) throws EntityException,
			UnknownNodeException {
		int position = 0;

		for (Subnode subnode : getSubnodesFromNode(nodeId)) {
			if (subnode.getPosition() > position) {
				position = subnode.getPosition();
			}
		}

		return ++position;
	}

	/**
	 * Gets the subnodes from tree.
	 * 
	 * @param treeId
	 *            the tree id
	 * @return the subnodes from tree
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownTreeException
	 *             the unknown tree exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	public Set<Subnode> getSubnodesFromTree(int treeId) throws EntityException,
			UnknownTreeException, UnknownNodeException {
		NodeRepository nr = new NodeRepository();
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Node node : nr.getNodes(treeId)) {
			for (Subnode subnode : sr.getSubnodes(node.getId())) {
				subnodes.add(subnode);
			}
		}

		return subnodes;
	}

	/**
	 * Gets the subnodes from node.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the subnodes from node
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	public Set<Subnode> getSubnodesFromNode(int nodeId) throws EntityException,
			UnknownNodeException {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}

		return subnodes;
	}

	/**
	 * Gets the subnode.
	 * 
	 * @param subnodeId
	 *            the subnode id
	 * @return the subnode
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 * @throws EntityException
	 *             the entity exception
	 */
	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException,
			EntityException {
		return sr.getSubnode(subnodeId);
	}

	/**
	 * Update subnode.
	 * 
	 * @param subnode
	 *            the subnode
	 * @return the subnode
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	public Subnode updateSubnode(Subnode subnode)
			throws UnknownSubnodeException, EntityException,
			UnknownNodeException {
		Subnode updatedSubnode = sr.getSubnode(subnode.getId());
		if (subnode.getNodeId() != 0) {
			updatedSubnode.setNodeId(subnode.getNodeId());
		}
		if (subnode.getTitle() != null) {
			updatedSubnode.setTitle(subnode.getTitle());
		}
		if (subnode.getPosition() != 0) {
			int oldPosition = updatedSubnode.getPosition();
			int newPosition = subnode.getPosition();
			updatedSubnode.setPosition(subnode.getPosition());
			changeOtherSubnodePositions(subnode, oldPosition, newPosition);
		}

		return sr.updateSubnode(updatedSubnode);
	}

	/**
	 * Change other subnode positions.
	 * 
	 * @param subnode
	 *            the subnode
	 * @param oldPosition
	 *            the old position
	 * @param newPosition
	 *            the new position
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 */
	private void changeOtherSubnodePositions(Subnode subnode, int oldPosition,
			int newPosition) throws EntityException, UnknownNodeException,
			UnknownSubnodeException {
		for (Subnode otherSubnode : sr.getSubnodes(subnode.getNodeId())) {
			if (otherSubnode.getId() != subnode.getId()) {

				if (oldPosition < newPosition) {
					if (otherSubnode.getPosition() > oldPosition
							&& otherSubnode.getPosition() <= newPosition) {
						otherSubnode.setPosition(subnode.getPosition() - 1);
						sr.updateSubnode(otherSubnode);
					}
				}

				if (oldPosition > newPosition) {
					if (otherSubnode.getPosition() >= newPosition
							&& otherSubnode.getPosition() < oldPosition) {
						otherSubnode.setPosition(subnode.getPosition() + 1);
						sr.updateSubnode(otherSubnode);
					}
				}
			}
		}
	}

	/**
	 * Delete subnode.
	 * 
	 * @param subnodeId
	 *            the subnode id
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	public void deleteSubnode(int subnodeId) throws UnknownSubnodeException,
			EntityException, UnknownNodeException {
		changeSubnodePositionOnDelete(subnodeId);
		sr.deleteSubnode(subnodeId);
	}

	/**
	 * Change subnode position on delete.
	 * 
	 * @param subnodeId
	 *            the subnode id
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	private void changeSubnodePositionOnDelete(int subnodeId)
			throws UnknownSubnodeException, EntityException,
			UnknownNodeException {
		Subnode delSubnode = getSubnode(subnodeId);

		for (Subnode subnode : getSubnodesFromNode(delSubnode.getNodeId())) {
			if (subnode.getPosition() > delSubnode.getPosition()) {
				subnode.setPosition(subnode.getPosition() - 1);
				sr.updateSubnode(subnode);
			}
		}
	}

	/**
	 * Rename subnode.
	 * 
	 * @param subnode
	 *            the subnode
	 * @return the subnode
	 * @throws UnknownSubnodeException
	 *             the unknown subnode exception
	 * @throws EntityException
	 *             the entity exception
	 */
	public Subnode renameSubnode(Subnode subnode)
			throws UnknownSubnodeException, EntityException {
		Subnode renamedSubnode = sr.getSubnode(subnode.getId());
		renamedSubnode.setTitle(subnode.getTitle());
		return sr.updateSubnode(renamedSubnode);
	}

	/**
	 * Drill up.
	 * 
	 * @param uid
	 *            the uid
	 * @param nodeId
	 *            the node id
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws UnknownUserException
	 *             the unknown user exception
	 */
	public Set<Subnode> drillUp(int uid, int nodeId) throws EntityException,
			UnknownNodeException, UnknownUserException {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), subnodes);
	}

	/**
	 * Recursive drill up.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param quantity
	 *            the quantity
	 * @param subnodes
	 *            the subnodes
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 */
	private Set<Subnode> recursiveDrillUp(int nodeId, int quantity,
			Set<Subnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (Subnode subnode : sr.getSiblingSubnodes(nodeId)) {
				subnodes.add(subnode);
			}
			for (Subnode subnode : sr.getParentSubnodes(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillUp(subnode.getNodeId(), quantity - 1,
						subnodes);
			}
		}
		return subnodes;
	}

	/**
	 * Drill down.
	 * 
	 * @param uid
	 *            the uid
	 * @param treeId
	 *            the tree id
	 * @param nodeId
	 *            the node id
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws UnknownUserException
	 *             the unknown user exception
	 */
	public Set<Subnode> drillDown(int uid, int treeId, int nodeId)
			throws EntityException, UnknownNodeException, UnknownUserException {
		if (nodeId == 0) {
			Node rootNode = new NodeRepository().getRoot(treeId);
			if (rootNode == null) {
				return null;
			}
			nodeId = rootNode.getId();
		}
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), subnodes);
	}

	/**
	 * Recursive drill down.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param quantity
	 *            the quantity
	 * @param subnodes
	 *            the subnodes
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 */
	private Set<Subnode> recursiveDrillDown(int nodeId, int quantity,
			Set<Subnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (Subnode subnode : sr.getFollowerSubnodes(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillDown(subnode.getNodeId(),
						quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	/**
	 * Copy subnodes.
	 * 
	 * @param uid
	 *            the uid
	 * @param nodeId
	 *            the project node id
	 * @param newNodeId
	 *            the new node id
	 * @throws UnknownTreeException 
	 * @throws UnknownUserException 
	 * @throws CreationException 
	 * @throws UnknownSubnodeException 
	 * @throws UnknownNodeException 
	 * @throws EntityException 
	 * @throws InterruptedException 
	 */
	public void copySubnodes(int uid, int treeId, int nodeId, int newNodeId) throws EntityException, UnknownNodeException, UnknownSubnodeException, CreationException, UnknownUserException, UnknownTreeException, InterruptedException {
		MediaWikiManager mwm = new MediaWikiManager();
		Set<Subnode> subnodeList = getSubnodesFromNode(nodeId);
		CountDownLatch subnodeLatch = new CountDownLatch(subnodeList.size());
		for (Subnode subnode : subnodeList) {
			WikiEntry swe = mwm.getKnowledgeSubnodeWikiEntry(subnode.getId());
			subnode.setWikititle(null);
			subnode.setNodeId(newNodeId);
			addSubnode(uid, treeId, subnode, swe.getWikiContentPlain(), subnodeLatch);
		}
		subnodeLatch.await();
	}
}