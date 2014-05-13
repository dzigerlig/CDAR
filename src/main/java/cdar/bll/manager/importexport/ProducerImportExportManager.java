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
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.TreeXml;
import cdar.bll.entity.WikiEntry;
import cdar.bll.entity.producer.Template;
import cdar.bll.entity.producer.TreeFull;
import cdar.bll.entity.producer.TreeSimple;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownEntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.NodeLinkRepository;
import cdar.dal.producer.NodeRepository;
import cdar.dal.producer.SubnodeRepository;
import cdar.dal.producer.TemplateRepository;
import cdar.dal.producer.XmlTreeRepository;

public class ProducerImportExportManager {
	private XmlTreeRepository xtr = new XmlTreeRepository();
	
	public Set<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException {
		Set<TreeXml> xmlTrees = new HashSet<TreeXml>();
		
		for (TreeXml xmlTree : xtr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}
		
		return xmlTrees;
	}
	
	private String getTreeSimpleXmlString(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException {
		TreeSimple ts = new TreeSimple(treeId);
		try {
			final Marshaller m = JAXBContext.newInstance(TreeSimple.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(ts, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getTreeFullXmlString(int treeId) throws EntityException, UnknownNodeException, UnknownUserException, UnknownTreeException, UnknownSubnodeException {
		TreeFull tf = new TreeFull(treeId);
		try {
			final Marshaller m = JAXBContext.newInstance(TreeFull.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(tf, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TreeSimple getTreeSimple(String xmlString) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TreeSimple.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			TreeSimple treeSimple = (TreeSimple) unmarshaller.unmarshal(reader);
			return treeSimple;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private TreeFull getTreeFull(String xmlString) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TreeFull.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			TreeFull treeFull = (TreeFull) unmarshaller.unmarshal(reader);
			return treeFull;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TreeXml addXmlTreeSimple(int uid, int treeId, String title, String xmlString) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException, UnknownXmlTreeException {
		xmlString = xmlString==null ? getTreeSimpleXmlString(treeId) : xmlString;
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(false);
		return xtr.createXmlTree(xmlTree);
	}

	public void deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		xtr.deleteXmlTree(xtr.getXmlTree(xmlTreeId));
	}

	public TreeXml getXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException {
		return xtr.getXmlTree(xmlTreeId);
	}


	public void cleanTree(int treeId) {
		NodeRepository nr = new NodeRepository();
		DirectoryRepository dr = new DirectoryRepository();
		TemplateRepository tr = new TemplateRepository();
		
		try {
			for (Node node : nr.getNodes(treeId)) {
				nr.deleteNode(node.getId());
			}
	
			for (Directory directory : dr.getDirectories(treeId)) {
				if (directory.getParentId()!=0) {
					dr.deleteDirectory(directory.getId());
				}
			}
			
			for (Template template : tr.getTemplates(treeId)) {
				tr.deleteTemplate(template.getId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setXmlTree(int xmlTreeId, boolean cleanTree) throws UnknownXmlTreeException, EntityException, UnknownTemplateException, UnknownDirectoryException, UnknownTreeException, CreationException, UnknownNodeException, UnknownUserException {
		TreeXml treeXml = getXmlTree(xmlTreeId);
		
		if (cleanTree) {
			cleanTree(treeXml.getTreeId());
		}
		
		if (treeXml.getIsFull()) {
			setFullXmlTree(treeXml);
		} else {
			setSimpleXmlTree(treeXml);
		}
	}

	private void setFullXmlTree(TreeXml treeXml) throws CreationException, UnknownUserException, EntityException, UnknownTreeException, UnknownDirectoryException, UnknownNodeException {
		TreeFull treeFull = getTreeFull(treeXml.getXmlString());
		DirectoryManager dm = new DirectoryManager();
		
		List<Directory> directoryList = new ArrayList<Directory>(treeFull.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId()).compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		Map<Integer, String> nodeWikiMapping = new HashMap<Integer, String>();
		Map<Integer, String> subnodeWikiMapping = new HashMap<Integer, String>();
		
		int rootDirectory = ((Directory)dm.getDirectories(treeXml.getTreeId()).toArray()[0]).getId();

		for (Directory directory : directoryList) {
			if (directory.getParentId()==0) {
				directoryMapping.put(directory.getId(), rootDirectory);
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setTreeId(treeXml.getTreeId());
				newDirectory.setParentId(directoryMapping.get(directory.getParentId()));
				newDirectory = dm.addDirectory(newDirectory);
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}
		
		NodeRepository nr = new NodeRepository();

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (treeFull.getNodes() != null) {
			for (Node node : treeFull.getNodes()) {
				Node newNode = new Node();
				newNode.setTreeId(treeXml.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode = nr.createNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
				nodeWikiMapping.put(node.getId(), newNode.getWikititle());
			}
		}
		
		SubnodeRepository sr = new SubnodeRepository();

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (treeFull.getSubnodes() != null) {
			for (Subnode subnode : treeFull.getSubnodes()) {
				Subnode newSubnode = new Subnode();
				newSubnode.setPosition(subnode.getPosition());
				newSubnode.setTitle(subnode.getTitle());
				newSubnode.setNodeId(nodeMapping.get(subnode.getNodeId()));
				newSubnode = sr.createSubnode(newSubnode);
				subnodeMapping.put(subnode.getId(), newSubnode.getId());
				subnodeWikiMapping.put(subnode.getId(), newSubnode.getWikititle());
			}
		}
		
		NodeLinkRepository nlr = new NodeLinkRepository();

		if (treeFull.getNodeLinks() != null) {
			for (NodeLink nodeLink : treeFull.getNodeLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(treeXml.getTreeId());
				newNodeLink.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				nlr.createNodeLink(newNodeLink);
			}
		}
		
		WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();
		
		if (treeFull.getWikiEntries() != null) {
			for (WikiEntry wikiEntry : treeFull.getWikiEntries()) {
				String wikititle = null;
				if (wikiEntry.getNodeId()!=0) {
					wikititle = nodeWikiMapping.get(wikiEntry.getNodeId());
				}
				
				if (wikiEntry.getSubnodeId()!=0) {
					wikititle = subnodeWikiMapping.get(wikiEntry.getSubnodeId());
				}
				
				String textPlain = wikiEntry.getWikiContentPlain();
				
				wikiHelper.addWikiEntry(wikititle, textPlain);

				MediaWikiCreationModel mwm = new MediaWikiCreationModel(treeXml.getUserId(), wikititle, textPlain, wikiHelper);
				mwm.start();
			}
		}
	}

	private void setSimpleXmlTree(TreeXml treeXml) throws CreationException, EntityException, UnknownTreeException, UnknownUserException, UnknownDirectoryException, UnknownNodeException {
		TreeSimple treeSimple = getTreeSimple(treeXml.getXmlString());
		DirectoryManager dm = new DirectoryManager();
		List<Directory> directoryList = new ArrayList<Directory>(treeSimple.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId()).compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		
		int rootDirectory = ((Directory)dm.getDirectories(treeXml.getTreeId()).toArray()[0]).getId();
		
		for (Directory directory : directoryList) {
			if (directory.getParentId()==0) {
				directoryMapping.put(directory.getId(), rootDirectory);
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setTreeId(treeXml.getTreeId());
				newDirectory.setParentId(directoryMapping.get(directory.getParentId()));
				newDirectory = dm.addDirectory(newDirectory);
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}
		
		NodeRepository nr = new NodeRepository();

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (treeSimple.getNodes() != null) {
			for (Node node : treeSimple.getNodes()) {
				Node newNode = new Node();
				newNode.setTreeId(treeXml.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setWikititle(node.getWikititle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode = nr.createNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
			}
		}
		
		SubnodeRepository sr = new SubnodeRepository();

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (treeSimple.getSubnodes() != null) {
			for (Subnode subnode : treeSimple.getSubnodes()) {
				Subnode newSubnode = new Subnode();
				newSubnode.setPosition(subnode.getPosition());
				newSubnode.setTitle(subnode.getTitle());
				newSubnode.setWikititle(subnode.getWikititle());
				newSubnode.setNodeId(nodeMapping.get(subnode.getNodeId()));
				newSubnode = sr.createSubnode(newSubnode);
				subnodeMapping.put(subnode.getId(), newSubnode.getId());
			}
		}
		
		NodeLinkRepository nlr = new NodeLinkRepository();
		
		if (treeSimple.getNodeLinks() != null) {
			for (NodeLink nodeLink : treeSimple.getNodeLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(treeXml.getTreeId());
				newNodeLink.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				nlr.createNodeLink(newNodeLink);
			}
		}
	}

	public TreeXml addXmlTreeFull(int uid, int treeId, String title, String xmlString) throws UnknownProjectTreeException, EntityException, UnknownXmlTreeException, UnknownNodeException, UnknownUserException, UnknownTreeException, UnknownSubnodeException {
		xmlString = xmlString==null ? getTreeFullXmlString(treeId) : xmlString;
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(true);
		return xtr.createXmlTree(xmlTree);
	}

	public TreeXml updateXmlTree(TreeXml treeXml) throws UnknownXmlTreeException, EntityException {
		TreeXml updatedTreeXml = xtr.getXmlTree(treeXml.getId());
		if (treeXml.getTitle()!=null) {
			updatedTreeXml.setTitle(treeXml.getTitle());
		}
		return xtr.updateXmlTree(updatedTreeXml);
	}
}
