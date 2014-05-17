package cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.bll.wiki.MediaWikiModel;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.NodeRepository;
import cdar.dal.producer.SubnodeRepository;
import cdar.dal.user.UserRepository;

public class SubnodeManager {
	private SubnodeRepository sr = new SubnodeRepository();

	public Subnode addSubnode(int uid, Subnode subnode) throws EntityException, UnknownNodeException, CreationException, UnknownUserException   {
		boolean createSubnode = true;
		if (subnode.getWikititle()!=null) {
			createSubnode = false;
		}
		subnode.setPosition(sr.getNextSubnodePosition(subnode.getNodeId()));
		subnode = sr.createSubnode(subnode);
		
		if (createSubnode) {
			String templateContent = null;
		
			if (templateContent == null) {
				templateContent = "== CDAR SUBNODE ==";
			}
		
			MediaWikiModel mwm = new MediaWikiModel();
			mwm.createWikiEntry(uid, subnode.getWikititle(), templateContent);
		}
		
		return subnode;
	}

	public Set<Subnode> getSubnodesFromTree(int treeId) throws EntityException, UnknownTreeException, UnknownNodeException   {
		NodeRepository nr = new NodeRepository();
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Node node : nr.getNodes(treeId)) {
			for (Subnode subnode : sr.getSubnodes(node.getId())) {
				subnodes.add(subnode);
			}
		}

		return subnodes;
	}

	public Set<Subnode> getSubnodesFromNode(int nodeId) throws EntityException, UnknownNodeException   {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}

		return subnodes;
	}

	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException, EntityException {
		return sr.getSubnode(subnodeId);
	}

	public Subnode updateSubnode(Subnode subnode) throws UnknownSubnodeException, EntityException, UnknownNodeException {
		Subnode updatedSubnode = sr.getSubnode(subnode.getId());
		if (subnode.getNodeId()!=0) {
			updatedSubnode.setNodeId(subnode.getNodeId());
		}
		if (subnode.getTitle()!=null) {
			updatedSubnode.setTitle(subnode.getTitle());
		}
		if (subnode.getPosition()!=0) {
			int oldPosition = updatedSubnode.getPosition();
			int newPosition = subnode.getPosition();
			updatedSubnode.setPosition(subnode.getPosition());
			
			changeOtherSubnodePositions(subnode, oldPosition, newPosition);
		}

		return sr.updateSubnode(updatedSubnode);
	}

	private void changeOtherSubnodePositions(Subnode subnode, int oldPosition,
			int newPosition) throws EntityException, UnknownNodeException,
			UnknownSubnodeException {
		for (Subnode otherSubnode : sr.getSubnodes(subnode.getNodeId())) {
			if (otherSubnode.getId()!=subnode.getId()) {
				
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

	public void deleteSubnode(int subnodeId) throws UnknownSubnodeException {
		sr.deleteSubnode(subnodeId);
	}

	public Subnode renameSubnode(Subnode subnode) throws UnknownSubnodeException, EntityException {
		Subnode renamedSubnode = sr.getSubnode(subnode.getId());
		renamedSubnode.setTitle(subnode.getTitle());
		return sr.updateSubnode(renamedSubnode);
	}

	public int getNextSubnodePosition(int nodeId) throws EntityException, UnknownNodeException   {
		return sr.getNextSubnodePosition(nodeId);
	}

	public Set<Subnode> drillUp(int uid, int nodeId) throws EntityException, UnknownNodeException, UnknownUserException {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), subnodes);
	}

	private Set<Subnode> recursiveDrillUp(int nodeId, int quantity,
			Set<Subnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (Subnode subnode : sr.getSiblingSubnode(nodeId)) {
				subnodes.add(subnode);
			}
			for (Subnode subnode : sr.getParentSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillUp(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	public Set<Subnode> drillDown(int uid, int nodeId) throws EntityException, UnknownNodeException, UnknownUserException  {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), subnodes);
	}

	private Set<Subnode> recursiveDrillDown(int nodeId, int quantity,
			Set<Subnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (Subnode subnode : sr.getFollowerSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillDown(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}
}