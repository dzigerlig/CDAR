package cdar.bll.manager.importexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.TreeXml;
import cdar.bll.entity.export.CDAR_TreeExportModel;
import cdar.bll.entity.export.CDAR_TreeSimple;
import cdar.bll.entity.producer.Template;
import cdar.bll.manager.consumer.ProjectTreeManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownEntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.producer.NodeLinkRepository;
import cdar.dal.producer.NodeRepository;
import cdar.dal.producer.SubnodeRepository;
import cdar.dal.producer.TemplateRepository;
import cdar.dal.producer.XmlTreeRepository;

public class ImportExportManager {
	private ProjectTreeManager ptm = new ProjectTreeManager();
	
	private DirectoryManager dm = new DirectoryManager();
	private NodeRepository nr = new NodeRepository();
	private NodeLinkRepository nlr = new NodeLinkRepository();
	private XmlTreeRepository xtr = new XmlTreeRepository();
	private TemplateRepository tr = new TemplateRepository();
	private SubnodeRepository sr = new SubnodeRepository();

	public Set<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException  {
		Set<TreeXml> xmlTrees = new HashSet<TreeXml>();

		for (TreeXml xmlTree : xtr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}

		return xmlTrees;
	}

	public TreeXml addXmlTree(int uid, int ktrid) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException, UnknownXmlTreeException    {
		CDAR_TreeExportModel tem = new CDAR_TreeExportModel();
		final String xmlString = tem.getTreeSimpleXmlString(ktrid);
		TreeXml xmlTree = new TreeXml();
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(ktrid);
		xmlTree.setXmlString(xmlString);
		return xtr.createXmlTree(xmlTree);
	}

	public void deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		xtr.deleteXmlTree(xtr.getXmlTree(xmlTreeId));
	}

	public TreeXml getXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException {
		return xtr.getXmlTree(xmlTreeId);
	}

	public TreeXml updateXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException, EntityException {
		TreeXml updatedXmlTree = getXmlTree(xmlTree.getId());
		updatedXmlTree.setXmlString(xmlTree.getXmlString());
		return xtr.updateXmlTree(updatedXmlTree);
	}

	public void cleanTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownDirectoryException, UnknownTreeException, UnknownTemplateException {
		NodeRepository nr = new NodeRepository();
		
		TreeXml xmlTree = getXmlTree(xmlTreeId);
		int treeId = xmlTree.getTreeId();
		for (Node node : nr.getNodes(treeId)) {
			nr.deleteNode(node.getId());
		}

		for (Directory directory : dm.getDirectories(treeId)) {
			if (directory.getParentId()!=0) {
				dm.deleteDirectory(directory.getId());
			}
		}

		for (Template template : tr.getTemplates(treeId)) {
			tr.deleteTemplate(template.getId());
		}
	}

	public boolean setXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException, UnknownTemplateException, UnknownDirectoryException, UnknownTreeException, CreationException, UnknownNodeException   {
		
		CDAR_TreeExportModel ctem = new CDAR_TreeExportModel();
		TreeXml xmlTree = getXmlTree(xmlTreeId);
		CDAR_TreeSimple cts = ctem.getTreeSimple(xmlTree.getXmlString());

		if (cts.getTemplates()!=null) {
			for (Template template : cts.getTemplates()) {
				Template newTemplate = new Template();
				newTemplate.setDecisionMade(template.getDecisionMade());
				newTemplate.setIsDefault(template.getIsDefault());
				newTemplate.setTemplatetext(template.getTemplatetext());
				newTemplate.setTitle(template.getTitle());
				newTemplate.setTreeId(template.getTreeId());
				tr.createTemplate(newTemplate);
			}
		}

		List<Directory> directoryList = new ArrayList<Directory>(
				cts.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId())
						.compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();

		for (Directory directory : directoryList) {
			if (directory.getParentId()==0) {
				directoryMapping.put(directory.getId(), directory.getId());
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setTreeId(directory.getTreeId());
				newDirectory.setParentId(directoryMapping.get(directory.getParentId()));
				newDirectory = dm.addDirectory(newDirectory);
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (cts.getNodes() != null) {
			for (Node node : cts.getNodes()) {
				Node newNode = new Node();
				newNode.setTreeId(node.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode = nr.createNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
			}
		}

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (cts.getSubnodes() != null) {
			for (Subnode subnode : cts.getSubnodes()) {
				Subnode newSubnode = new Subnode();
				newSubnode.setPosition(subnode.getPosition());
				newSubnode.setTitle(subnode.getTitle());
				newSubnode.setNodeId(nodeMapping.get(subnode.getNodeId()));
				newSubnode = sr.createSubnode(newSubnode);
				subnodeMapping.put(subnode.getId(), newSubnode.getId());
			}
		}

		if (cts.getLinks() != null) {
			for (NodeLink nodeLink : cts.getLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(nodeLink.getTreeId());
				newNodeLink
						.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink
						.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink
							.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				nlr.createNodeLink(newNodeLink);
			}
		}

		return true;
	}
}
