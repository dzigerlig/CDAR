package cdar.bll.model.knowledgeproducer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeLinkDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class LinkModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public List<NodeLink> getLinks() {
		List<NodeLink> ln = new ArrayList<NodeLink>();

		Set<KnowledgeNodeLinkDao>  kl= kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks();
		System.out.println(kl.size());

		/*for (KnowledgeNodeLinkDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks()) {
			ln.add(new NodeLink(knd));
		}
		*/
		/*
		ln.add(new NodeLink(0, new Date(), new Date(), 1, 2));
		ln.add(new NodeLink(1, new Date(), new Date(), 1, 3));
		ln.add(new NodeLink(2, new Date(), new Date(), 1, 4));
		ln.add(new NodeLink(3, new Date(), new Date(), 1, 5));*/
		return ln;
	}
	
	public List<NodeLink> getLinksWithFollowers(int Id, int quantityOfFollowers) {
		List<NodeLink> ln = new ArrayList<NodeLink>();
		for (KnowledgeNodeLinkDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks()) {
			ln.add(new NodeLink(knd));
		}
		/*
		ln.add(new NodeLink(0, new Date(), new Date(), 0, 1));
		ln.add(new NodeLink(1, new Date(), new Date(), 0, 2));
		ln.add(new NodeLink(2, new Date(), new Date(), 0, 3));
		ln.add(new NodeLink(3, new Date(), new Date(), 0, 4));*/
		return ln;
	}
	
	public void removeLinkById(String id)
	{
		System.out.println("Link "+id+" removed");
	}
	
	public NodeLink addLink(NodeLink nl)
	{
		return new NodeLink(kpdc.addKnowledgeNodeLink(nl.getSourceId(), nl.getTargetId(), nl.getRefTreeId()));
	}
}
