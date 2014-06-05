package ch.cdar.bll.manager.importexport;

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

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.TreeXml;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.entity.producer.Template;
import ch.cdar.bll.entity.producer.TreeFull;
import ch.cdar.bll.entity.producer.TreeSimple;
import ch.cdar.bll.helpers.WikiEntryConcurrentHelper;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.producer.TemplateManager;
import ch.cdar.bll.wiki.MediaWikiCreationModel;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownDirectoryException;
import ch.cdar.dal.exceptions.UnknownEntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTemplateException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.exceptions.UnknownXmlTreeException;
import ch.cdar.dal.producer.DirectoryRepository;
import ch.cdar.dal.producer.NodeLinkRepository;
import ch.cdar.dal.producer.NodeRepository;
import ch.cdar.dal.producer.SubnodeRepository;
import ch.cdar.dal.producer.TemplateRepository;
import ch.cdar.dal.producer.XmlTreeRepository;

/**
 * The Class ProducerImportExportManager.
 */
public class ProducerImportExportManager {
	
	/** The Xml Tree Repository. */
	private XmlTreeRepository xtr = new XmlTreeRepository();
	
	/**
	 * Gets the xml trees.
	 *
	 * @param treeId the tree id
	 * @return the xml trees
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownEntityException the unknown entity exception
	 */
	public Set<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException {
		Set<TreeXml> xmlTrees = new HashSet<TreeXml>();
		
		for (TreeXml xmlTree : xtr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}
		
		return xmlTrees;
	}
	
	/**
	 * Gets the tree simple xml string.
	 *
	 * @param treeId the tree id
	 * @return the tree simple xml string
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	private String getTreeSimpleXmlString(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownProjectTreeException {
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
	
	/**
	 * Gets the tree full xml string.
	 *
	 * @param treeId the tree id
	 * @return the tree full xml string
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	private String getTreeFullXmlString(int treeId) throws EntityException, UnknownNodeException, UnknownUserException, UnknownTreeException, UnknownSubnodeException, UnknownProjectTreeException {
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
	
	/**
	 * Gets the tree simple.
	 *
	 * @param xmlString the xml string
	 * @return the tree simple
	 */
	private TreeSimple getTreeSimple(String xmlString) {
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
	
	/**
	 * Gets the tree full.
	 *
	 * @param xmlString the xml string
	 * @return the tree full
	 */
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
	
	/**
	 * Adds the xml tree simple.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param title the title
	 * @param xmlString the xml string
	 * @return the tree xml
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public TreeXml addXmlTreeSimple(int uid, int treeId, String title, String xmlString) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException, UnknownXmlTreeException, UnknownProjectTreeException {
		xmlString = xmlString==null ? getTreeSimpleXmlString(treeId) : xmlString;
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(false);
		return xtr.createXmlTree(xmlTree);
	}

	/**
	 * Delete xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws Exception the exception
	 */
	public void deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		xtr.deleteXmlTree(xtr.getXmlTree(xmlTreeId));
	}

	/**
	 * Gets the xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @return the xml tree
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 */
	public TreeXml getXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException {
		return xtr.getXmlTree(xmlTreeId);
	}


	/**
	 * Clean tree.
	 *
	 * @param treeId the tree id
	 */
	private void cleanTree(int treeId) {
		NodeRepository nr = new NodeRepository();
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		TemplateManager tr = new TemplateManager();
		
		try {
			for (Node node : nr.getNodes(treeId)) {
				nr.deleteNode(node.getId());
			}
	
			Directory rootDirectory = new Directory();
			for (Directory directory : dm.getDirectories(treeId)) {
				if (directory.getParentId()==0) {
					rootDirectory=directory;
					dm.deleteDirectory(directory.getId());
				}
			}
			
			dm.addDirectory(rootDirectory);
			
			for (Template template : tr.getKnowledgeTemplates(treeId)) {
				tr.deleteTemplate(template.getId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sets the xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @param cleanTree the clean tree
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTemplateException the unknown template exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws CreationException the creation exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 */
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

	/**
	 * Sets the full xml tree.
	 *
	 * @param treeXml the new full xml tree
	 * @throws CreationException the creation exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	private void setFullXmlTree(TreeXml treeXml) throws CreationException, UnknownUserException, EntityException, UnknownTreeException, UnknownDirectoryException, UnknownNodeException {
		TreeFull treeFull = getTreeFull(treeXml.getXmlString());
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		
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
		int rootDirectory = 0;
		for (Directory directory : dm.getDirectories(treeXml.getTreeId())) {
			if (directory.getParentId()==0) {
				rootDirectory= directory.getId();
			}
		}
		

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

	/**
	 * Sets the simple xml tree.
	 *
	 * @param treeXml the new simple xml tree
	 * @throws CreationException the creation exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	private void setSimpleXmlTree(TreeXml treeXml) throws CreationException, EntityException, UnknownTreeException, UnknownUserException, UnknownDirectoryException, UnknownNodeException {
		TreeSimple treeSimple = getTreeSimple(treeXml.getXmlString());
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		List<Directory> directoryList = new ArrayList<Directory>(treeSimple.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId()).compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		
		int rootDirectory = 0;
		for (Directory directory : dm.getDirectories(treeXml.getTreeId())) {
			if (directory.getParentId()==0) {
				rootDirectory= directory.getId();
			}
		}
				
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

	/**
	 * Adds the xml tree full.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param title the title
	 * @param xmlString the xml string
	 * @return the tree xml
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
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

	/**
	 * Update xml tree.
	 *
	 * @param treeXml the tree xml
	 * @return the tree xml
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 */
	public TreeXml updateXmlTree(TreeXml treeXml) throws UnknownXmlTreeException, EntityException {
		TreeXml updatedTreeXml = xtr.getXmlTree(treeXml.getId());
		if (treeXml.getTitle()!=null) {
			updatedTreeXml.setTitle(treeXml.getTitle());
		}
		return xtr.updateXmlTree(updatedTreeXml);
	}
}
