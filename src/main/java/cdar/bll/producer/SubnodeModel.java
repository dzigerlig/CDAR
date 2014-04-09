package cdar.bll.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;

public class SubnodeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public void removeSubnodeById(int id) {
		pdc.getSubnode(id).delete();
	}

	public Subnode addSubnode(Subnode sN) {
		SubnodeDao subnode = new SubnodeDao(sN.getKnid(), sN.getTitle());
		return new Subnode(subnode.create());
	}

	//whole tree
	public Set<Subnode> getSubnodes(int treeId) {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (NodeDao node : pdc.getNodes(treeId)) {
			for (SubnodeDao subnode : pdc.getSubnodes(node.getId())) {
				subnodes.add(new Subnode(subnode));
			}
		}
		return subnodes;
	}
	
	public Set<Subnode> getSubnodesOfNode(int nodeId) {
		Set<Subnode> subnodes = new HashSet<Subnode>();
		
		for (SubnodeDao subnode : pdc.getSubnodes(nodeId)) {
			subnodes.add(new Subnode(subnode));
		}
		
		return subnodes;
	}
}