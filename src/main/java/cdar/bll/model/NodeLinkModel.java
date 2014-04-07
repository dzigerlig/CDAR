package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.NodeLink;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class NodeLinkModel {
	private ProducerDaoController pdc = new ProducerDaoController();


	public Set<NodeLink> getLinks(int treeid) {
		Set<NodeLink>  nodelink= new HashSet<NodeLink>();

		for (NodeLinkDao nld : pdc.getNodeLinks(treeid)) {
			nodelink.add(new NodeLink(nld));
		}
		return nodelink;
	}
	
	public void removeLinkById(int id)
	{
		NodeLinkDao nld = pdc.getNodeLink(id);
		nld.delete();
	}
	
	public NodeLink addLink(NodeLink nl)
	{
		NodeLinkDao nld = new NodeLinkDao();
		nld.setKsnid(nl.getRefSubNodeId());
		nld.setKtrid(nl.getRefTreeId());
		nld.setSourceid(nl.getSourceId());
		nld.setTargetid(nl.getTargetId());
		return new NodeLink(nld.create());
	}
}
