package cdar.bll.manager.producer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.producer.NodeLinkRepository;

public class NodeLinkManager {
	private NodeLinkRepository nlr = new NodeLinkRepository();

	public Set<NodeLink> getNodeLinks(int treeId) throws SQLException {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinks(treeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public void deleteNodeLink(int nodeLinkId) throws Exception
	{
		nlr.deleteNodeLink(nodeLinkId);
	}
	
	public NodeLink addNodeLink(int ktrid, int sourceId, int targetId, int ksnid) throws Exception
	{
		NodeLink nodeLink = new NodeLink();
		nodeLink.setTreeId(ktrid);
		nodeLink.setSourceId(sourceId);
		nodeLink.setTargetId(targetId);
		nodeLink.setSubnodeId(ksnid);
		return nlr.createNodeLink(nodeLink);
	}

	public NodeLink updateNodeLink(NodeLink nodelink) throws Exception {
		NodeLink updatedNodeLink = nlr.getNodeLink(nodelink.getId());
		updatedNodeLink.setSubnodeId(nodelink.getSubnodeId());
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
	
	public Set<NodeLink> zoomUp(int nodeId) throws SQLException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomUp(nodeId, 2, links);
	}
	
	public Set<NodeLink> zoomDown(int nodeId) throws SQLException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomDown(nodeId, 2, links);
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
