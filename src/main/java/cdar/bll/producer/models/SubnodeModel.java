package cdar.bll.producer.models;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.producer.Subnode;
import cdar.dal.persistence.jdbc.producer.NodeRepository;
import cdar.dal.persistence.jdbc.producer.ProducerDaoRepository;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;

public class SubnodeModel {
	private ProducerDaoRepository pdc = new ProducerDaoRepository();

	public Subnode addSubnode(int knid, String title) {
		SubnodeDao subnode = new SubnodeDao(knid,
				pdc.getNextSubnodePosition(knid), title);
		return new Subnode(subnode.create());
	}

	// whole tree
	public Set<Subnode> getSubnodesFromTree(int treeId) throws SQLException {
		NodeRepository nr = new NodeRepository();
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (Node node : nr.getNodes(treeId)) {
			for (SubnodeDao subnode : pdc.getSubnodes(node.getId())) {
				subnodes.add(new Subnode(subnode));
			}
		}

		return subnodes;
	}

	public Set<Subnode> getSubnodesFromNode(int nodeId) {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (SubnodeDao subnode : pdc.getSubnodes(nodeId)) {
			subnodes.add(new Subnode(subnode));
		}

		return subnodes;
	}

	public Subnode getSubnode(int subnodeid) {
		return new Subnode(pdc.getSubnode(subnodeid));
	}

	public boolean changeSubnodePosition(int id, boolean up) {
		int oldPosition = getSubnode(id).getPosition();
		int newPosition = up ? oldPosition - 1 : oldPosition + 1;

		Set<Subnode> subnodes = getSubnodesFromNode(getSubnode(id).getKnid());

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

	public Subnode updateSubnode(Subnode subnode) {
		SubnodeDao subnodedao = pdc.getSubnode(subnode.getId());
		subnodedao.setKnid(subnode.getKnid());
		subnodedao.setTitle(subnode.getTitle());
		subnodedao.setPosition(subnode.getPosition());
		return new Subnode(subnodedao.update());
	}

	public boolean deleteSubnode(int id) {
		return pdc.getSubnode(id).delete();
	}

	public boolean renameSubnode(Subnode subnode) {
		SubnodeDao subnodedao = pdc.getSubnode(subnode.getId());
		subnodedao.setTitle(subnode.getTitle());
		Subnode retSubnode = new Subnode(subnodedao.update());
		if (retSubnode.getId() == -1) {
			return false;
		}
		{
			return true;
		}

	}

	public int getNextSubnodePosition(int nodeid) {
		return pdc.getNextSubnodePosition(nodeid);
	}

	public boolean moveSubnodeUp(Subnode subnode) {
		return changeSubnodePosition(subnode.getId(), true);
	}

	public boolean moveSubnodeDown(Subnode subnode) {
		return changeSubnodePosition(subnode.getId(), false);
	}

	public Set<Subnode> zoomUp(int nodeid) {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (SubnodeDao subnode : pdc.getSubnodes(nodeid)) {
			subnodes.add(new Subnode(subnode));
		}
		return rekZoomUp(nodeid, 2, subnodes);
	}

	private Set<Subnode> rekZoomUp(int nodeid, int quantity,
			Set<Subnode> subnodes) {
		if (quantity > 0) {
			for (SubnodeDao subnode : pdc.getSiblingSubnode(nodeid)) {
				subnodes.add(new Subnode(subnode));
			}
			for (SubnodeDao subnode : pdc.getParentSubnode(nodeid)) {
				subnodes.add(new Subnode(subnode));
				subnodes = rekZoomUp(subnode.getKnid(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	public Set<Subnode> zoomDown(int nodeid) {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		for (SubnodeDao subnode : pdc.getSubnodes(nodeid)) {
			subnodes.add(new Subnode(subnode));
		}
		return rekZoomDown(nodeid, 2, subnodes);
	}

	private Set<Subnode> rekZoomDown(int nodeid, int quantity,
			Set<Subnode> subnodes) {
		if (quantity > 0) {
			for (SubnodeDao node : pdc.getFollowerSubnode(nodeid)) {
				subnodes.add(new Subnode(node));
				subnodes = rekZoomDown(node.getKnid(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}
}