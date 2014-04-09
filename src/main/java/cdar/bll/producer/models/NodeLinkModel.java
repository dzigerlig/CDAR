package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.NodeLink;
import cdar.dal.persistence.jdbc.producer.NodeDao;
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
	
	public NodeLink addLink(int ktrid, int sourceid, int targetid, int ksnid)
	{
		NodeLinkDao nld = new NodeLinkDao();
		nld.setKtrid(ktrid);
		nld.setSourceid(sourceid);
		nld.setTargetid(targetid);
		nld.setKsnid(ksnid);
		return new NodeLink(nld.create());
	}

	public NodeLink updateLink(NodeLink nl) {
		NodeLinkDao nodeLink = pdc.getNodeLink(nl.getId());
		nodeLink.setKtrid(nl.getKtrid());
		nodeLink.setSourceid(nl.getSourceId());
		nodeLink.setTargetid(nl.getTargetId());
		nodeLink.setKsnid(nl.getKsnid());
		return new NodeLink(nodeLink.update());	
	}

	public NodeLink getLink(int id) {
		return new NodeLink(pdc.getNodeLink(id));
	}
}
