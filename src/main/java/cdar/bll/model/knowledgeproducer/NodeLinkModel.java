package cdar.bll.model.knowledgeproducer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeLinkDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class NodeLinkModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public Set<NodeLink> getLinks() {
		Set<NodeLink>  kl= new HashSet<NodeLink>();

		for (KnowledgeNodeLinkDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks()) {
			kl.add(new NodeLink(knd));
		}
		return kl;
	}
	
	public List<NodeLink> getLinksWithFollowers(int Id, int quantityOfFollowers) {
		List<NodeLink> ln = new ArrayList<NodeLink>();
		for (KnowledgeNodeLinkDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks()) {
			ln.add(new NodeLink(knd));
		}
		return ln;
	}
	
	public void removeLinkById(int id)
	{
		System.out.println("remove link"+id);

		kpdc.removeKnowledgeNodeLink(id);
	}
	
	public NodeLink addLink(NodeLink nl)
	{
		return new NodeLink(kpdc.addKnowledgeNodeLink(nl.getSourceId(), nl.getTargetId(), nl.getRefTreeId()));
	}
}
