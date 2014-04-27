package cdar.bll.producer.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoRepository;

public class NodeLinkModel {
	private ProducerDaoRepository pdc = new ProducerDaoRepository();


	public Set<NodeLink> getNodeLinks(int treeid) {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLinkDao nld : pdc.getNodeLinks(treeid)) {
			nodeLinks.add(new NodeLink(nld));
		}
		
		return nodeLinks;
	}
	
	public boolean deleteNodeLink(int id)
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

	public List<NodeLink> getNodeLinksBySubnode(int subnodeid) {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLinkDao nld : pdc.getNodeLinksBySubnode(subnodeid)) {
			nodeLinks.add(new NodeLink(nld));
		}
		
		return nodeLinks;
	}
	
	public Set<NodeLink> zoomUp(int nodeid) {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return rekZoomUp(nodeid, 2, links);
	}

	private Set<NodeLink> rekZoomUp(int nodeid, int quantity, Set<NodeLink> links) {
		if (quantity > 0) {
			for (NodeLinkDao link : pdc.getSiblingNodeLinks(nodeid)) {
				links.add(new NodeLink(link));
			}
			for (NodeLinkDao link : pdc.getParentNodeLinks(nodeid)) {
				links.add(new NodeLink(link));
				links=rekZoomUp(link.getSourceid(), quantity-1, links);
			}
		}
		return links;
	}
	
	public Set<NodeLink> zoomDown(int nodeid) {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return rekZoomDown(nodeid, 2, links);
	}

	private Set<NodeLink> rekZoomDown(int nodeid, int quantity, Set<NodeLink> links) {
		if (quantity > 0) {
			for (NodeLinkDao link : pdc.getFollowerNodeLinks(nodeid)) {
				links.add(new NodeLink(link));
				links = rekZoomDown(link.getTargetid(), quantity-1, links);
			}
		}
		return links;
	}
}
