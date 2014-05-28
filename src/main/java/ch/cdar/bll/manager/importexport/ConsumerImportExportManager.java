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
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.TreeXml;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.entity.consumer.Comment;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.bll.entity.consumer.ProjectTreeFull;
import ch.cdar.bll.entity.consumer.ProjectTreeSimple;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.consumer.CommentManager;
import ch.cdar.bll.wiki.MediaWikiCreationModel;
import ch.cdar.bll.wiki.WikiEntryConcurrentHelper;
import ch.cdar.dal.consumer.ProjectNodeLinkRepository;
import ch.cdar.dal.consumer.ProjectNodeRepository;
import ch.cdar.dal.consumer.ProjectSubnodeRepository;
import ch.cdar.dal.consumer.ProjectTreeXmlRepository;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownCommentException;
import ch.cdar.dal.exceptions.UnknownDirectoryException;
import ch.cdar.dal.exceptions.UnknownEntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTemplateException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.exceptions.UnknownXmlTreeException;

/**
 * The Class ConsumerImportExportManager.
 */
public class ConsumerImportExportManager {
	
	/** The ptxr. */
	private ProjectTreeXmlRepository ptxr = new ProjectTreeXmlRepository();

	/**
	 * Gets the xml trees.
	 *
	 * @param treeId the tree id
	 * @return the xml trees
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownEntityException the unknown entity exception
	 */
	public Set<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException  {
		Set<TreeXml> xmlTrees = new HashSet<TreeXml>();

		for (TreeXml xmlTree : ptxr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}

		return xmlTrees;
	}
	
	/**
	 * Gets the tree simple xml string.
	 *
	 * @param treeId the tree id
	 * @return the tree simple xml string
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 */
	private String getTreeSimpleXmlString(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownTreeException, UnknownUserException {
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
	
	/**
	 * Gets the tree full xml string.
	 *
	 * @param treeId the tree id
	 * @return the tree full xml string
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 */
	private String getTreeFullXmlString(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownProjectNodeException, UnknownProjectSubnodeException, UnknownTreeException, UnknownUserException {
		ProjectTreeFull pts = new ProjectTreeFull(treeId);
		try {
			final Marshaller m = JAXBContext.newInstance(ProjectTreeFull.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(pts, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the project tree simple.
	 *
	 * @param xmlString the xml string
	 * @return the project tree simple
	 */
	private ProjectTreeSimple getProjectTreeSimple(String xmlString) {
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
	
	/**
	 * Gets the project tree full.
	 *
	 * @param xmlString the xml string
	 * @return the project tree full
	 */
	private ProjectTreeFull getProjectTreeFull(String xmlString) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProjectTreeFull.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			ProjectTreeFull treeFull = (ProjectTreeFull) unmarshaller.unmarshal(reader);
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
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 */
	public TreeXml addXmlTreeSimple(int uid, int treeId, String title, String xmlString) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException, UnknownXmlTreeException, UnknownProjectTreeException, UnknownProjectNodeLinkException    {
		xmlString = xmlString==null ? getTreeSimpleXmlString(treeId) : xmlString;
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(false);
		return ptxr.createXmlTree(xmlTree);
	}

	/**
	 * Delete xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws Exception the exception
	 */
	public void deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		ptxr.deleteXmlTree(ptxr.getXmlTree(xmlTreeId));
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
		return ptxr.getXmlTree(xmlTreeId);
	}


	/**
	 * Clean tree.
	 *
	 * @param projectTreeId the project tree id
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownTemplateException the unknown template exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownCommentException the unknown comment exception
	 */
	private void cleanTree(int projectTreeId) throws UnknownXmlTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownDirectoryException, UnknownTreeException, UnknownTemplateException, UnknownProjectTreeException, UnknownProjectNodeException, UnknownCommentException {
		ProjectNodeRepository pnr = new ProjectNodeRepository();
		DirectoryManager pdm = new DirectoryManager(UserRole.CONSUMER);
		CommentManager cm = new CommentManager();
		
		try {
			for (ProjectNode projectNode : pnr.getNodes(projectTreeId)) {
				pnr.deleteNode(projectNode.getId());
			}
	
			for (Directory directory : pdm.getDirectories(projectTreeId)) {
				if (directory.getParentId()!=0) {
					pdm.deleteDirectory(directory.getId());
				}
			}
			
			for (Comment comment : cm.getCommentsByTree(projectTreeId)) {
				cm.deleteComment(comment.getId());
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
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownCommentException the unknown comment exception
	 */
	public void setXmlTree(int xmlTreeId, boolean cleanTree) throws UnknownXmlTreeException, EntityException, UnknownTemplateException, UnknownDirectoryException, UnknownTreeException, CreationException, UnknownNodeException, UnknownUserException, UnknownProjectTreeException, UnknownProjectNodeException, UnknownCommentException   {
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
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	private void setFullXmlTree(TreeXml treeXml) throws CreationException, UnknownProjectTreeException, UnknownProjectNodeException, UnknownUserException, EntityException, UnknownDirectoryException {
		ProjectTreeFull projectTreeFull = getProjectTreeFull(treeXml.getXmlString());
		DirectoryManager pdm = new DirectoryManager(UserRole.CONSUMER);
		
		List<Directory> directoryList = new ArrayList<Directory>(projectTreeFull.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId()).compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		Map<Integer, String> nodeWikiMapping = new HashMap<Integer, String>();
		Map<Integer, String> subnodeWikiMapping = new HashMap<Integer, String>();
		
		int rootDirectory = ((Directory)pdm.getDirectories(treeXml.getTreeId()).toArray()[0]).getId();

		for (Directory directory : directoryList) {
			if (directory.getParentId()==0) {
				directoryMapping.put(directory.getId(), rootDirectory);
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setTreeId(treeXml.getTreeId());
				newDirectory.setParentId(directoryMapping.get(directory.getParentId()));
				newDirectory = pdm.addDirectory(newDirectory);
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}
		
		ProjectNodeRepository pnr = new ProjectNodeRepository();

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (projectTreeFull.getProjectNodes() != null) {
			for (ProjectNode node : projectTreeFull.getProjectNodes()) {
				ProjectNode newNode = new ProjectNode();
				newNode.setTreeId(treeXml.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode.setStatus(node.getStatus());
				newNode = pnr.createNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
				nodeWikiMapping.put(node.getId(), newNode.getWikititle());
			}
		}
		
		ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (projectTreeFull.getProjectSubnodes() != null) {
			for (ProjectSubnode subnode : projectTreeFull.getProjectSubnodes()) {
				ProjectSubnode newProjectSubnode = new ProjectSubnode();
				newProjectSubnode.setPosition(subnode.getPosition());
				newProjectSubnode.setTitle(subnode.getTitle());
				newProjectSubnode.setNodeId(nodeMapping.get(subnode.getNodeId()));
				newProjectSubnode.setStatus(subnode.getStatus());
				newProjectSubnode = psr.createSubnode(newProjectSubnode);
				subnodeMapping.put(subnode.getId(), newProjectSubnode.getId());
				subnodeWikiMapping.put(subnode.getId(), newProjectSubnode.getWikititle());
			}
		}
		
		ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

		if (projectTreeFull.getLinks() != null) {
			for (NodeLink nodeLink : projectTreeFull.getLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(treeXml.getTreeId());
				newNodeLink.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				pnlr.createNodeLink(newNodeLink);
			}
		}
		
		WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();
		
		if (projectTreeFull.getWikiEntries() != null) {
			for (WikiEntry wikiEntry : projectTreeFull.getWikiEntries()) {
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
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws CreationException the creation exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	private void setSimpleXmlTree(TreeXml treeXml) throws UnknownProjectTreeException, CreationException, UnknownProjectNodeException, EntityException, UnknownUserException, UnknownDirectoryException {
		ProjectTreeSimple projectTreeSimple = getProjectTreeSimple(treeXml.getXmlString());
		DirectoryManager pdm = new DirectoryManager(UserRole.CONSUMER);
		List<Directory> directoryList = new ArrayList<Directory>(projectTreeSimple.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentId()).compareTo(directory2.getParentId());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		
		int rootDirectory = ((Directory)pdm.getDirectories(treeXml.getTreeId()).toArray()[0]).getId();
		
		for (Directory directory : directoryList) {
			if (directory.getParentId()==0) {
				directoryMapping.put(directory.getId(), rootDirectory);
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setTreeId(treeXml.getTreeId());
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
				newNode.setTreeId(treeXml.getTreeId());
				newNode.setDirectoryId(directoryMapping.get(node.getDirectoryId()));
				newNode.setTitle(node.getTitle());
				newNode.setWikititle(node.getWikititle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode.setStatus(node.getStatus());
				newNode = pnr.createNode(newNode);
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
				newProjectSubnode = psr.createSubnode(newProjectSubnode);
				subnodeMapping.put(subnode.getId(), newProjectSubnode.getId());
			}
		}
		
		ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();
		
		if (projectTreeSimple.getLinks() != null) {
			for (NodeLink nodeLink : projectTreeSimple.getLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setTreeId(treeXml.getTreeId());
				newNodeLink.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getSubnodeId() != 0) {
					newNodeLink.setSubnodeId(subnodeMapping.get(nodeLink.getSubnodeId()));
				}
				pnlr.createNodeLink(newNodeLink);
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
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public TreeXml addXmlTreeFull(int uid, int treeId, String title, String xmlString) throws UnknownProjectTreeException, EntityException, UnknownXmlTreeException, UnknownProjectNodeLinkException, UnknownProjectNodeException, UnknownProjectSubnodeException, UnknownTreeException, UnknownUserException {
		xmlString = xmlString==null ? getTreeFullXmlString(treeId) : xmlString;
		TreeXml xmlTree = new TreeXml();
		xmlTree.setTitle(title);
		xmlTree.setUserId(uid);
		xmlTree.setTreeId(treeId);
		xmlTree.setXmlString(xmlString);
		xmlTree.setIsFull(true);
		return ptxr.createXmlTree(xmlTree);
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
		TreeXml updatedTreeXml = ptxr.getXmlTree(treeXml.getId());
		if (treeXml.getTitle()!=null) {
			updatedTreeXml.setTitle(treeXml.getTitle());
		}
		return ptxr.updateXmlTree(updatedTreeXml);
	}

}
