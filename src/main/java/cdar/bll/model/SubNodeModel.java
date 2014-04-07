package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.SubNode;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubNodeDao;

public class SubNodeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public void removeSubNodeById(int id) {
		pdc.getSubNode(id).delete();
	}

	public SubNode addSubNode(SubNode sN) {
		SubNodeDao subnode = new SubNodeDao(sN.getKnid(), sN.getTitle());
		return new SubNode(subnode.create());
	}

	//whole tree
	public Set<SubNode> getSubNodes(int treeId) {
		Set<SubNode> subnodes = new HashSet<SubNode>();

		for (NodeDao node : pdc.getNodes(treeId)) {
			for (SubNodeDao subnode : pdc.getSubNodes(node.getId())) {
				subnodes.add(new SubNode(subnode));
			}
		}
		return subnodes;
	}
	
	public Set<SubNode> getSubNodes(int treeId, int nodeId) {
		Set<SubNode> subnodes = new HashSet<SubNode>();
		
		for (SubNodeDao subnode : pdc.getSubNodes(nodeId)) {
			subnodes.add(new SubNode(subnode));
		}
		
		return subnodes;
	}

}
