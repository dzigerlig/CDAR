package cdar.bll.manager.producer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.producer.NodeRepository;
import cdar.dal.producer.SubnodeRepository;

public class SubnodeManager {
	private SubnodeRepository sr = new SubnodeRepository();

	public Subnode addSubnode(Subnode subnode) throws Exception {
		subnode.setPosition(sr.getNextSubnodePosition(subnode.getNodeId()));
		return sr.createSubnode(subnode);
	}

	// whole tree
	public Set<Subnode> getSubnodesFromTree(int treeId) throws SQLException {
		NodeRepository nr = new NodeRepository();
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Node node : nr.getNodes(treeId)) {
			for (Subnode subnode : sr.getSubnodes(node.getId())) {
				subnodes.add(subnode);
			}
		}

		return subnodes;
	}

	public Set<Subnode> getSubnodesFromNode(int nodeId) throws SQLException {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}

		return subnodes;
	}

	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException {
		return sr.getSubnode(subnodeId);
	}

	public boolean changeSubnodePosition(int id, boolean up) throws SQLException, UnknownSubnodeException {
		int oldPosition = getSubnode(id).getPosition();
		int newPosition = up ? oldPosition - 1 : oldPosition + 1;

		Set<Subnode> subnodes = getSubnodesFromNode(getSubnode(id).getNodeId());

		if (subnodes.size() == 0 || newPosition > subnodes.size()
				|| newPosition < 1) {
			return false;
		} else {

			for (Subnode subnode : subnodes) {
				if (subnode.getId() == id) {
					subnode.setPosition(newPosition);
					updateSubnode(subnode);
				} else {
					if (oldPosition < newPosition) {
						if (subnode.getPosition() > oldPosition
								&& subnode.getPosition() <= newPosition) {
							subnode.setPosition(subnode.getPosition() - 1);
							updateSubnode(subnode);
						}
					}

					if (oldPosition > newPosition) {
						if (subnode.getPosition() >= newPosition
								&& subnode.getPosition() < oldPosition) {
							subnode.setPosition(subnode.getPosition() + 1);
							updateSubnode(subnode);
						}
					}

				}
			}
			return true;
		}
	}

	public Subnode updateSubnode(Subnode subnode) throws UnknownSubnodeException {
		Subnode updatedSubnode = sr.getSubnode(subnode.getId());
		if (subnode.getNodeId()!=0) {
			updatedSubnode.setNodeId(subnode.getNodeId());
		}
		if (subnode.getTitle()!=null) {
			updatedSubnode.setTitle(subnode.getTitle());
		}
		if (subnode.getPosition()!=0) {
			updatedSubnode.setPosition(subnode.getPosition());
		}
		return sr.updateSubnode(updatedSubnode);
	}

	public void deleteSubnode(int subnodeId) throws UnknownSubnodeException {
		sr.deleteSubnode(subnodeId);
	}

	public Subnode renameSubnode(Subnode subnode) throws UnknownSubnodeException {
		Subnode renamedSubnode = sr.getSubnode(subnode.getId());
		renamedSubnode.setTitle(subnode.getTitle());
		return sr.updateSubnode(renamedSubnode);
	}

	public int getNextSubnodePosition(int nodeId) throws SQLException {
		return sr.getNextSubnodePosition(nodeId);
	}

	public boolean moveSubnodeUp(Subnode subnode) throws SQLException, UnknownSubnodeException {
		return changeSubnodePosition(subnode.getId(), true);
	}

	public boolean moveSubnodeDown(Subnode subnode) throws SQLException, UnknownSubnodeException {
		return changeSubnodePosition(subnode.getId(), false);
	}

	public Set<Subnode> zoomUp(int nodeId) throws SQLException {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveZoomUp(nodeId, 2, subnodes);
	}

	private Set<Subnode> recursiveZoomUp(int nodeId, int quantity,
			Set<Subnode> subnodes) {
		if (quantity > 0) {
			for (Subnode subnode : sr.getSiblingSubnode(nodeId)) {
				subnodes.add(subnode);
			}
			for (Subnode subnode : sr.getParentSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveZoomUp(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	public Set<Subnode> zoomDown(int nodeId) throws SQLException {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (Subnode subnode : sr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveZoomDown(nodeId, 2, subnodes);
	}

	private Set<Subnode> recursiveZoomDown(int nodeId, int quantity,
			Set<Subnode> subnodes) {
		if (quantity > 0) {
			for (Subnode subnode : sr.getFollowerSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveZoomDown(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}
}