package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.NodeLink;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class NodeLinkModel {
	private ProducerDaoController pdc = new ProducerDaoController();


	public Set<NodeLink> getNodeLinks(int treeid) {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLinkDao nld : pdc.getNodeLinks(treeid)) {
			nodeLinks.add(new NodeLink(nld));
		}
		
		return nodeLinks;
	}
	
	public boolean removeNodeLink(int id)
	{
		NodeLinkDao nld = pdc.getNodeLink(id);
		return nld.delete();
	}
	
	public NodeLink addNodeLink(int ktrid, int sourceid, int targetid, int ksnid)
	{
		NodeLinkDao nld = new NodeLinkDao();
		nld.setKtrid(ktrid);
		nld.setSourceid(sourceid);
		nld.setTargetid(targetid);
		nld.setKsnid(ksnid);
		return new NodeLink(nld.create());
	}

	public NodeLink updateNodeLink(NodeLink nl) {
		NodeLinkDao nodeLink = pdc.getNodeLink(nl.getId());
		nodeLink.setKsnid(nl.getKsnid());
		return new NodeLink(nodeLink.update());	
	}

	public NodeLink getNodeLink(int id) {
		return new NodeLink(pdc.getNodeLink(id));
	}
}
