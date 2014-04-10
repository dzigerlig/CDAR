package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Subnode;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;

public class SubnodeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public Subnode addSubnode(int knid, String title) {
		SubnodeDao subnode = new SubnodeDao(knid, title);
		return new Subnode(subnode.create());
	}
 
	//whole tree
	public Set<Subnode> getSubnodesFromTree(int treeId) {
		Set<Subnode> subnodes = new HashSet<Subnode>();

		for (NodeDao node : pdc.getNodes(treeId)) {
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
	
	public Subnode updateSubnode(Subnode subnode) {
		SubnodeDao subnodedao = pdc.getSubnode(subnode.getId());
		subnodedao.setKnid(subnode.getKnid());
		subnodedao.setTitle(subnode.getTitle());
		return new Subnode(subnodedao.update());
	}
	
	public boolean removeSubnode(int id) {
		return pdc.getSubnode(id).delete();
	}
}