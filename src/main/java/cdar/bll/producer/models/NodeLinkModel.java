package cdar.bll.producer.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.producer.NodeLink;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.persistence.jdbc.producer.NodeLinkRepository;

public class NodeLinkModel {
	private NodeLinkRepository nlr = new NodeLinkRepository();

	public Set<NodeLink> getNodeLinks(int treeId) throws SQLException {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinks(treeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public boolean deleteNodeLink(int nodeLinkId) throws Exception
	{
		NodeLink nodeLink = nlr.getNodeLink(nodeLinkId);
		return nlr.deleteNodeLink(nodeLink);
	}
	
	public NodeLink addNodeLink(int ktrid, int sourceid, int targetid, int ksnid) throws Exception
	{
		NodeLink nodeLink = new NodeLink();
		nodeLink.setKtrid(ktrid);
		nodeLink.setSourceId(sourceid);
		nodeLink.setTargetId(targetid);
		nodeLink.setKsnid(ksnid);
		return nlr.createNodeLink(nodeLink);
	}

	public NodeLink updateNodeLink(NodeLink nodelink) throws Exception {
		NodeLink updatedNodeLink = nlr.getNodeLink(nodelink.getId());
		updatedNodeLink.setKsnid(nodelink.getKsnid());
		return nlr.updateNodeLink(updatedNodeLink);
	}

	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException {
		return nlr.getNodeLink(nodeLinkId);
	}

	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws SQLException {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public Set<NodeLink> zoomUp(int nodeid) throws SQLException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomUp(nodeid, 2, links);
	}
	
	public Set<NodeLink> zoomDown(int nodeid) throws SQLException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomDown(nodeid, 2, links);
	}

	private Set<NodeLink> recursiveZoomUp(int nodeId, int quantity, Set<NodeLink> links) throws SQLException {
		if (quantity > 0) {
			for (NodeLink nodeLink : nlr.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : nlr.getParentNodeLinks(nodeId)) {
				links.add(nodeLink);
				links=recursiveZoomUp(nodeLink.getSourceId(), quantity-1, links);
			}
		}
		return links;
	}
	
	private Set<NodeLink> recursiveZoomDown(int nodeId, int quantity, Set<NodeLink> links) throws SQLException {
		if (quantity > 0) {
			for (NodeLink nodeLink : nlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveZoomDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}
}
