package cdar.bll.manager.importexport;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cdar.bll.entity.Directory;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.TreeXml;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.entity.consumer.ProjectTreeSimple;
import cdar.bll.manager.consumer.ProjectDirectoryManager;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.consumer.ProjectTreeXmlRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownEntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.producer.NodeRepository;

public class ConsumerImportExportManager {
	public ProjectTreeXmlRepository ptxr = new ProjectTreeXmlRepository();

	public Set<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException  {
		Set<TreeXml> xmlTrees = new HashSet<TreeXml>();

		for (TreeXml xmlTree : ptxr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}

		return xmlTrees;
	}
	
	private String getTreeSimpleXmlString(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException {
		ProjectTreeSimple pts = new ProjectTreeSimple(treeId);
		try {
			final Marshaller m = JAXBContext.newInstance(ProjectTreeSimple.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(pts, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ProjectTreeSimple getProjectTreeSimple(String xmlString) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProjectTreeSimple.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			ProjectTreeSimple treeSimple = (ProjectTreeSimple) unmarshaller.unmarshal(reader);
			return treeSimple;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TreeXml addXmlTreeSimple(int uid, int treeId, String title) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException, UnknownXmlTreeException, UnknownProjectTreeException, UnknownProjectNodeLinkException    {
		final String xmlString = getTreeSimpleXmlString(treeId);
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(false);
		return ptxr.createXmlTree(xmlTree);
	}

	public void deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		ptxr.deleteXmlTree(ptxr.getXmlTree(xmlTreeId));
	}

	public TreeXml getXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException {
		return ptxr.getXmlTree(xmlTreeId);
	}


	public void cleanTree(int projectTreeId) throws UnknownXmlTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownDirectoryException, UnknownTreeException, UnknownTemplateException, UnknownProjectTreeException, UnknownProjectNodeException {
		ProjectNodeRepository pnr = new ProjectNodeRepository();
		ProjectDirectoryManager pdm = new ProjectDirectoryManager();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(projectTreeId)) {
			pnr.deleteProjectNode(projectNode.getId());
		}

		for (Directory directory : pdm.getDirectories(projectTreeId)) {
			if (directory.getParentId()!=0) {
				pdm.deleteDirectory(directory.getId());
			}
		}

		//todo
		//Comments
	}

	public void setXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException, UnknownTemplateException, UnknownDirectoryException, UnknownTreeException, CreationException, UnknownNodeException, UnknownUserException, UnknownProjectTreeException, UnknownProjectNodeException   {
		TreeXml treeXml = getXmlTree(xmlTreeId);
		
		cleanTree(treeXml.getTreeId());
		
		if (treeXml.getIsFull()) {
			//todo setFull
		} else {
			setSimpleXmlTree(treeXml);
		}
	}

	private void setSimpleXmlTree(TreeXml treeXml) throws UnknownProjectTreeException, CreationException, UnknownProjectNodeException {
		ProjectTreeSimple projectTreeSimple = getProjectTreeSimple(treeXml.getXmlString());
		
		ProjectDirectoryManager pdm = new ProjectDirectoryManager();
		
		List<Directory> directoryList = new ArrayList<Directory>(projectTreeSimple.getDirectories());

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
				newDirectory = pdm.addDirectory(newDirectory);
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}
		
		ProjectNodeRepository pnr = new ProjectNodeRepository();

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (projectTreeSimple.getProjectNodes() != null) {
			for (ProjectNode node : projectTreeSimple.getProjectNodes()) {
				ProjectNode newNode = new ProjectNode();
				newNode.setTreeId(node.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setWikititle(node.getWikititle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode.setStatus(node.getStatus());
				newNode = pnr.createProjectNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
			}
		}
		
		ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (projectTreeSimple.getProjectSubnodes() != null) {
			for (ProjectSubnode subnode : projectTreeSimple.getProjectSubnodes()) {
				ProjectSubnode newProjectSubnode = new ProjectSubnode();
				newProjectSubnode.setPosition(subnode.getPosition());
				newProjectSubnode.setTitle(subnode.getTitle());
				newProjectSubnode.setWikititle(subnode.getWikititle());
				newProjectSubnode.setNodeId(nodeMapping.get(subnode.getNodeId()));
				newProjectSubnode.setStatus(subnode.getStatus());
				newProjectSubnode = psr.createProjectSubnode(newProjectSubnode);
				subnodeMapping.put(subnode.getId(), newProjectSubnode.getId());
			}
		}
		
		ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

		if (projectTreeSimple.getLinks() != null) {
			for (NodeLink nodeLink : projectTreeSimple.getLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(nodeLink.getTreeId());
				newNodeLink.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				pnlr.createProjectNodeLink(newNodeLink);
			}
		}
	}

	public Object addXmlTreeFull(int uid, int treeId, String title) throws UnknownProjectTreeException, EntityException, UnknownXmlTreeException, UnknownProjectNodeLinkException {
		//todo change logic
		final String xmlString = getTreeSimpleXmlString(treeId);
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(true);
		return ptxr.createXmlTree(xmlTree);
	}
}
