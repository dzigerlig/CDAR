package cdar.jersey.integrationtest;

import static org.junit.Assert.*;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.entity.ChangesWrapper;
import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.entity.producer.Template;
import cdar.pl.controller.UserController;
import cdar.pl.controller.producer.DirectoryController;
import cdar.pl.controller.producer.NodeController;
import cdar.pl.controller.producer.NodeLinkController;
import cdar.pl.controller.producer.SubnodeController;
import cdar.pl.controller.producer.TreeController;
import cdar.pl.controller.producer.TemplateController;

public class TestKnowledgeTreeController extends JerseyTest {

	private final String USERNAME = "testuser";
	private final String PASSWORD = "testpassword";
	private final String TREENAME = "testtree";
	private final String UID = "uid";
	private final String ACCESSTOKEN = "accesstoken";
	private final int NOTEXISTINGID = 999999999;
	private int userId;
	private int treeid;
	private String accesstoken;

	@Override
	protected Application configure() {
		return new ResourceConfig(TreeController.class,
				UserController.class, NodeController.class,
				TemplateController.class, DirectoryController.class,
				NodeLinkController.class,
				SubnodeController.class);
	}

	/*
	 * private String getAuthString() { return String.format("%s/%d",
	 * accesstoken, userId); }
	 */

	@Before
	public void createUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user = target("users")
				.request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON),
						Response.class).readEntity(User.class);
		user = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class)
				.readEntity(User.class);
		userId = user.getId();
		accesstoken = user.getAccesstoken();
		Response quantityOfTreesBeforeAddRequest = target("ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesBeforeAdd = quantityOfTreesBeforeAddRequest
				.readEntity(Set.class).size();
		Tree addTree = new Tree();
		addTree.setTitle(TREENAME);
		Response createdTreeResponse = target("ktrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree createdTree = createdTreeResponse.readEntity(Tree.class);
		treeid = createdTree.getId();
		int quantityOfTreesAfterAdd = target("/ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		assertEquals(201, createdTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 1, quantityOfTreesAfterAdd);
		assertNotEquals(0, createdTree.getId());
	}

	@After
	public void testDeleteUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		int quantityOfTreesBeforeDelete = target("/ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		Response deletedTreeResponse = target("/ktrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfTreesAfterDelete = target("/ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();
		User deleteUser = new User();
		deleteUser.setId(userId);
		target("users/delete")
				.queryParam("password", PASSWORD)
				.queryParam("username", USERNAME)
				.request()
				.post(Entity.entity(deleteUser, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(200, deletedTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeDelete - 1,
				quantityOfTreesAfterDelete);
	}

	@Test
	public void testDelteNotExistingTree() {
		Tree deleteTree = new Tree();
		deleteTree.setId(NOTEXISTINGID);
		Response deletedTreeResponse = target("/ktrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTreeResponse.getStatus());
	}

	@Test
	public void testGetTree() {
		Response testTreeResponse = target("/ktrees/" + treeid).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Tree testTree = testTreeResponse.readEntity(Tree.class);
		assertEquals(TREENAME, testTree.getTitle());
		assertNotEquals(0, testTree.getId());
		assertEquals(200, testTreeResponse.getStatus());
	}

	@Test
	public void testEditTree() {
		Tree editTree = new Tree();
		editTree.setTitle("editTree");
		Response editedTreeResponse = target("ktrees/" + treeid)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(editTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree editedTree = getTree(treeid);
		assertEquals(200, editedTreeResponse.getStatus());
		assertEquals("editTree", editedTree.getTitle());
		assertNotEquals(0, editedTree.getId());
	}

	@Test
	public void testGetAllTrees() {
		int quantityOfTreesBeforeAdd = target("/ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree addedtree1 = addTree(TREENAME);
		Tree addedtree2 = addTree(TREENAME);

		Response quantityOfTreesAfterAddResponse = target("/ktrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesAfterAdd = quantityOfTreesAfterAddResponse
				.readEntity(Set.class).size();
		deleteTree(addedtree1.getId());
		deleteTree(addedtree2.getId());
		assertEquals(200, quantityOfTreesAfterAddResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 2, quantityOfTreesAfterAdd);
	}

	@Test
	public void testAddAndDeleteTemplate() {
		int quantityOfTemplatesBeforeAdd = target(
				"/ktrees/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeId(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");

		Response addedTemplateResponse = target(
				"/ktrees/" + treeid + "/templates")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Response.class);
		Template addedTemplate = addedTemplateResponse
				.readEntity(Template.class);

		int quantityOfTemplatesAfterAdd = target(
				"/ktrees/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)

				.get(Response.class).readEntity(Set.class).size();

		Response deletedTemplateRequest = target(
				"/ktrees/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
						Response.class);

		int quantityOfTemplatesAfterDelete = target(
				"/ktrees/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		assertEquals(201, addedTemplateResponse.getStatus());
		assertEquals(200, deletedTemplateRequest.getStatus());
		assertEquals(quantityOfTemplatesBeforeAdd + 1,
				quantityOfTemplatesAfterAdd);
		assertEquals(quantityOfTemplatesBeforeAdd,
				quantityOfTemplatesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingTemplate() {
		Template deleteTemplate = new Template();
		deleteTemplate.setId(NOTEXISTINGID);
		Response deletedTemplateResponse = target(
				"/ktrees/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTemplate, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTemplateResponse.getStatus());
	}

	@Test
	public void testGetTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Response getTemplateResponse = target(
				"/ktrees/" + treeid + "/templates/" + addedTemplate.getId())
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		addedTemplate = getTemplateResponse.readEntity(Template.class);
		deleteTemplate(addedTemplate.getId());
		assertTrue(addedTemplate.getId() > 0);
		assertEquals(200, getTemplateResponse.getStatus());
	}

	@Test
	public void testGetAllTemplates() {
		int quantityOfTemplatesBeforeAdd = target(
				"/ktrees/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();
		Template addedTemplate1 = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Template addedTemplate2 = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Response quantityOfTemplatesAfterAddResponse = target(
				"/ktrees/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTemplateAfterAdd = quantityOfTemplatesAfterAddResponse
				.readEntity(Set.class).size();
		deleteTemplate(addedTemplate1.getId());
		deleteTemplate(addedTemplate2.getId());
		assertEquals(200, quantityOfTemplatesAfterAddResponse.getStatus());
		assertEquals(quantityOfTemplatesBeforeAdd + 2,
				quantityOfTemplateAfterAdd);
	}

	@Test
	public void testEditTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		int templateid = addedTemplate.getId();
		addedTemplate.setTemplatetext(TREENAME);
		addedTemplate.setTitle(USERNAME);
		addedTemplate.setId(0);
		Response editTemplateResponse = target(
				"/ktrees/" + treeid + "/templates/" + templateid)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
						Response.class);
		Template editTemplate = getTemplate(templateid);
		deleteTemplate(editTemplate.getId());
		assertEquals(200, editTemplateResponse.getStatus());
		assertEquals(TREENAME, editTemplate.getTemplatetext());
		assertEquals(USERNAME, editTemplate.getTitle());
	}

	@Test
	public void testAddAndDeleteDirectories() {
		int quantityOfDirectoriesBeforeAdd = target(
				"/ktrees/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addDirectory = new Directory();
		addDirectory.setTreeId(treeid);
		addDirectory.setParentId(0);
		Response addedDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory addedDirectory = addedDirectoryResponse
				.readEntity(Directory.class);
		Directory addSubDirectory = new Directory();
		addSubDirectory.setTreeId(treeid);
		addSubDirectory.setParentId(addedDirectory.getId());
		Response addedSubDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addSubDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory addedSubDirectory = addedSubDirectoryResponse
				.readEntity(Directory.class);
		int quantityOfDirectoriesAfterAdd = target(
				"/ktrees/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubDirectory,
						MediaType.APPLICATION_JSON), Response.class);
		Response deletedDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfDirectoriesAfterDelete = target(
				"/ktrees/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		assertEquals(201, addedDirectoryResponse.getStatus());
		assertEquals(201, addedSubDirectoryResponse.getStatus());
		assertEquals(200, deletedDirectoryResponse.getStatus());
		assertEquals(200, deletedSubDirectoryResponse.getStatus());
		assertEquals(treeid, addedDirectory.getTreeId());
		assertEquals(treeid, addedSubDirectory.getTreeId());
		assertEquals(0, addedDirectory.getParentId());
		assertEquals(addedDirectory.getId(), addedSubDirectory.getParentId());

		assertEquals(quantityOfDirectoriesBeforeAdd + 2,
				quantityOfDirectoriesAfterAdd);
		assertEquals(quantityOfDirectoriesBeforeAdd,
				quantityOfDirectoriesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingDirectory() {
		Directory deleteDirectory = new Directory();
		deleteDirectory.setId(NOTEXISTINGID);
		Response deletedDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(deleteDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedDirectoryResponse.getStatus());
	}

	@Test
	public void testGetDirectory() {
		Directory addedDirectory1 = addDirectory(treeid, 0);
		Response getDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/" + addedDirectory1.getId())
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Directory getDirectory = getDirectoryResponse
				.readEntity(Directory.class);
		deleteDirectory(addedDirectory1.getId());
		assertEquals(200, getDirectoryResponse.getStatus());
		assertNotEquals(null, getDirectory);
	}

	@Test
	public void testGetAllDirectories() {
		int quantityOfDirectoriesBefore = target(
				"/ktrees/" + treeid + "/directories").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addedDirectory1 = addDirectory(treeid, 0);
		Directory addedDirectory2 = addDirectory(treeid, 0);
		Response quantityOfDirectoriesAfterResponse = target(
				"/ktrees/" + treeid + "/directories").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfDirectoriesAfter = quantityOfDirectoriesAfterResponse
				.readEntity(Set.class).size();
		deleteDirectory(addedDirectory1.getId());
		deleteDirectory(addedDirectory2.getId());
		assertEquals(200, quantityOfDirectoriesAfterResponse.getStatus());
		assertEquals(quantityOfDirectoriesBefore + 2,
				quantityOfDirectoriesAfter);
	}

	@Test
	public void testEditDirectories() {
		Directory addedParentDirectory = addDirectory(treeid, 0);
		Directory addedDirectory = addDirectory(treeid, 0);
		addedDirectory.setTitle(USERNAME);
		addedDirectory.setParentId(addedParentDirectory.getId());
		int addedDirectoryId=addedDirectory.getId();
		addedDirectory.setId(0);
		Response updatedDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/" + addedDirectoryId)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory updatedDirectory = getDirectory(addedDirectoryId);
		deleteDirectory(addedDirectoryId);
		deleteDirectory(addedParentDirectory.getId());
		assertEquals(200, updatedDirectoryResponse.getStatus());
		assertEquals(USERNAME, updatedDirectory.getTitle());
		assertEquals(treeid, updatedDirectory.getTreeId());
		assertEquals(addedParentDirectory.getId(),
				updatedDirectory.getParentId());
	}

	@Test
	public void testAddAndDeleteNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target("/ktrees/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(addedDirectory.getId());
		Response addedNodeResponse = target("/ktrees/" + treeid + "/nodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class);
		Node addedNode = addedNodeResponse.readEntity(Node.class);

		int quantityOfNodesAfterAdd = target("/ktrees/" + treeid + "/nodes")
				.request().get(Response.class).readEntity(Set.class).size();
		Response deletedNodeResponse = target(
				"/ktrees/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNode, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfNodesAfterDelete = target("/ktrees/" + treeid + "/nodes")
				.request().get(Response.class).readEntity(Set.class).size();
		deleteDirectory(addedDirectory.getId());
		assertEquals(201, addedNodeResponse.getStatus());
		assertEquals(200, deletedNodeResponse.getStatus());
		assertEquals(treeid, addedNode.getTreeId());
		assertEquals(addedDirectory.getId(), addedNode.getDirectoryId());
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterAdd);
		assertEquals(quantityOfNodesBeforeAdd, quantityOfNodesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingNode() {
		Node deleteNode = new Node();
		deleteNode.setId(NOTEXISTINGID);
		Response deletedNodeResponse = target(
				"/ktrees/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteNode, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedNodeResponse.getStatus());
	}

	@Test
	public void testGetNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Response receivedNodeResponse = target(
				"/ktrees/treeid/nodes/" + addedNode.getId()).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Node receivedNode = receivedNodeResponse.readEntity(Node.class);
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, receivedNodeResponse.getStatus());
		assertEquals(treeid, receivedNode.getTreeId());
		assertEquals(addedDirectory.getId(), receivedNode.getDirectoryId());
	}

	@Test
	public void testGetAllNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target("/ktrees/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		Response quantityOfNodesAfterAddResponse = target(
				"/ktrees/" + treeid + "/nodes/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfNodesAfterAdd = quantityOfNodesAfterAddResponse
				.readEntity(Set.class).size();
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(200, quantityOfNodesAfterAddResponse.getStatus());
		assertEquals(quantityOfNodesBeforeAdd + 2, quantityOfNodesAfterAdd);
	}

	@Test
	public void testEditNode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		addedDirectory.setTitle(USERNAME);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		addedNode.setTitle(TREENAME);
		addedNode.setDynamicTreeFlag(1);
		int nodeId = addedNode.getId();
		addedNode.setId(0);
		Response updatedNodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + nodeId)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNode, MediaType.APPLICATION_JSON),
						Response.class);
		Node updatedNode = getNode(nodeId);
		deleteNode(nodeId);
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, updatedNodeResponse.getStatus());
		assertEquals(TREENAME, updatedNode.getTitle());
		assertEquals(treeid, updatedNode.getTreeId());
		assertEquals(1, updatedNode.getDynamicTreeFlag());
		assertEquals(addedDirectory.getId(), updatedNode.getDirectoryId());
	}

	@Test
	public void testAddAndDeleteNodeLink() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodeLinksBeforeAdd = target(
				"/ktrees/" + treeid + "/links").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();

		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(addedNode1.getId());
		addTestNodeLink.setTargetId(addedNode2.getId());
		Response addedNodeLinkResponse = target("/ktrees/" + treeid + "/links")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class);
		NodeLink addedNodeLink = addedNodeLinkResponse
				.readEntity(NodeLink.class);

		int quantityOfNodeLinksAfterAdd = target(
				"/ktrees/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();

		Response deleteNodeLinkResponse = target(
				"/ktrees/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNodeLink, MediaType.APPLICATION_JSON),
						Response.class);

		int quantityOfNodeLinksAfterDelete = target(
				"/ktrees/" + treeid + "/links").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Set.class).size();
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(201, addedNodeLinkResponse.getStatus());
		assertEquals(200, deleteNodeLinkResponse.getStatus());
		assertEquals(treeid, addedNodeLink.getTreeId());
		assertEquals(addedNode1.getId(), addedNodeLink.getSourceId());
		assertEquals(addedNode2.getId(), addedNodeLink.getTargetId());
		assertEquals(quantityOfNodeLinksBeforeAdd + 1,
				quantityOfNodeLinksAfterAdd);
		assertEquals(quantityOfNodeLinksBeforeAdd,
				quantityOfNodeLinksAfterDelete);
	}

	@Test
	public void testDeleteNotExistingNodeLink() {
		NodeLink deleteNodeLink = new NodeLink();
		deleteNodeLink.setId(NOTEXISTINGID);
		Response deleteNodeLinkResponse = target(
				"/ktrees/" + treeid + "/links/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteNodeLink, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deleteNodeLinkResponse.getStatus());
	}

	@Test
	public void testGetAllNodeLinks() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		Response quantityOfNodeLinksBeforeAddResponse = target(
				"/ktrees/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfNodeLinksBeforeAdd = quantityOfNodeLinksBeforeAddResponse
				.readEntity(Set.class).size();
		NodeLink addedNodeLink1 = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		NodeLink addedNodeLink2 = addNodeLink(treeid, addedNode2.getId(),
				addedNode1.getId());

		int quantityOfNodeLinksAfterAdd = target(
				"/ktrees/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Set.class).size();
		deleteNodeLink(addedNodeLink1.getId());
		deleteNodeLink(addedNodeLink2.getId());
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, quantityOfNodeLinksBeforeAddResponse.getStatus());
		assertEquals(quantityOfNodeLinksBeforeAdd + 2,
				quantityOfNodeLinksAfterAdd);
	}

	@Test
	public void testAddAndDeleteSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				"/ktrees/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setNodeId(addedNode.getId());
		addTestSubnode.setTitle(TREENAME);
		Response addedSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class);
		Subnode addedSubnode = addedSubnodeResponse.readEntity(Subnode.class);

		int quantityOfSubnodesAfterAdd = target(
				"/ktrees/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubnode, MediaType.APPLICATION_JSON),
						Response.class);

		int quantityOfSubnodesAfterDelete = target(
				"/ktrees/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, deletedSubnodeResponse.getStatus());
		assertEquals(201, addedSubnodeResponse.getStatus());
		assertEquals(addedNode.getId(), addedSubnode.getNodeId());
		assertEquals(TREENAME, addedSubnode.getTitle());
		assertEquals(quantityOfSubnodesBeforeAdd + 1,
				quantityOfSubnodesAfterAdd);
		assertEquals(quantityOfSubnodesBeforeAdd, quantityOfSubnodesAfterDelete);
	}

	@Test
	public void testDeleteNonExistingSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Subnode deleteSubnode = new Subnode();
		deleteSubnode.setId(NOTEXISTINGID);
		Response deleteSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteSubnode, MediaType.APPLICATION_JSON),
						Response.class);

		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(400, deleteSubnodeResponse.getStatus());
	}

	@Test
	public void testGetSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		Subnode addedSubnode = addSubnode(addedNode.getId(), PASSWORD);

		Response getSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/" + addedSubnode.getId()).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Subnode getSubnode = getSubnodeResponse.readEntity(Subnode.class);
		deleteSubnode(addedSubnode.getId(), addedNode.getId());

		deleteNode(addedNode.getId());

		deleteDirectory(addedDirectory.getId());
		assertNotEquals(null, getSubnode);
		assertEquals(200, getSubnodeResponse.getStatus());
	}

	@Test
	public void testGetSubnodesOfNode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		Response quantityOfSubnodesAfterAddResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfSubnodesAfterAdd = quantityOfSubnodesAfterAddResponse
				.readEntity(Set.class).size();

		deleteSubnode(addedSubnode1.getId(), addedNode.getId());
		deleteSubnode(addedSubnode2.getId(), addedNode.getId());

		deleteNode(addedNode.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(200, quantityOfSubnodesAfterAddResponse.getStatus());
		assertEquals(quantityOfSubnodesBeforeAdd + 2,
				quantityOfSubnodesAfterAdd);
	}

	@Test
	public void testGetAllSubnodesOfTree() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				"/ktrees/" + treeid + "/subnodes").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		Response quantityOfSubnodesAfterAddResponse = target(
				"/ktrees/" + treeid + "/subnodes").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfSubnodesAfterAdd = quantityOfSubnodesAfterAddResponse
				.readEntity(Set.class).size();

		deleteSubnode(addedSubnode1.getId(), addedNode.getId());
		deleteSubnode(addedSubnode2.getId(), addedNode.getId());

		deleteNode(addedNode.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(200, quantityOfSubnodesAfterAddResponse.getStatus());
		assertEquals(quantityOfSubnodesBeforeAdd + 2,
				quantityOfSubnodesAfterAdd);
	}

	@Test
	public void testEditSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());
		Subnode addedSubnode = addSubnode(addedNode1.getId(), PASSWORD);
		addedSubnode.setNodeId(addedNode2.getId());
		addedSubnode.setPosition(2);
		int subnodeId = addedSubnode.getId();
		addedSubnode.setId(0);
		Response updatedSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode1.getId()
						+ "/subnodes/" + subnodeId)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubnode, MediaType.APPLICATION_JSON),
						Response.class);
		Subnode updatedSubnode = getSubnode(addedNode1.getId(), subnodeId);
		deleteSubnode(subnodeId, addedNode1.getId());

		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(addedNode2.getId(), updatedSubnode.getNodeId());
		assertEquals(2, updatedSubnode.getPosition());
		assertNotEquals(null, updatedSubnode);
		assertEquals(200, updatedSubnodeResponse.getStatus());
	}

	@Test
	public void testRenameSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Subnode addedSubnode = addSubnode(addedNode.getId(), PASSWORD);
		addedSubnode.setTitle(USERNAME);
		Response updatedSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/rename")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubnode, MediaType.APPLICATION_JSON),
						Response.class);
		Subnode getSubnode = getSubnode(addedNode.getId(), addedSubnode.getId());
		deleteSubnode(addedSubnode.getId(), addedNode.getId());

		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(USERNAME, getSubnode.getTitle());
		assertEquals(200, updatedSubnodeResponse.getStatus());
	}

	private Tree addTree(String treename) {
		Tree addTree = new Tree();
		addTree.setTitle(treename);
		return target("/ktrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Tree.class);
	}

	private boolean deleteTree(int treeid) {
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		return target("/ktrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Tree getTree(int id) {
		Response testTreeResponse = target("/ktrees/" + id).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		return testTreeResponse.readEntity(Tree.class);
	}

	private Template addTemplate(int treeid, String title, String templatetext) {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeId(treeid);
		addTestTemplate.setTitle(title);
		addTestTemplate.setTemplatetext(templatetext);
		return target("/ktrees/" + treeid + "/templates")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Template.class);
	}

	private boolean deleteTemplate(int templateid) {
		Template deleteTemplate = new Template();
		deleteTemplate.setId(templateid);
		return target("/ktrees/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTemplate, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	};

	private Template getTemplate(int templateid) {
		Response getTemplateResponse = target(
				"/ktrees/" + treeid + "/templates/" + templateid).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		return getTemplateResponse.readEntity(Template.class);
	}

	private Directory addDirectory(int treeid, int parentid) {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setTreeId(treeid);
		addTestDirectory.setParentId(parentid);
		return target("/ktrees/" + treeid + "/directories")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestDirectory,
						MediaType.APPLICATION_JSON), Response.class)
				.readEntity(Directory.class);
	}

	private boolean deleteDirectory(int directoryid) {
		Directory deleteDirectory = new Directory();
		deleteDirectory.setId(directoryid);
		return target("/ktrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(deleteDirectory, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Directory getDirectory(int directroyId) {
		Response getDirectoryResponse = target(
				"/ktrees/" + treeid + "/directories/" + directroyId).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		return getDirectoryResponse.readEntity(Directory.class);
	}

	private Node addNode(int treeid, int did) {
		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(did);
		return target("/ktrees/" + treeid + "/nodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Node.class);
	}

	private boolean deleteNode(int nodeid) {
		Node deleteNode = new Node();
		deleteNode.setId(nodeid);
		return target("/ktrees/" + treeid + "/nodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Node getNode(int nodeId) {
		return target("/ktrees/treeid/nodes/" + nodeId).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Node.class);
	}

	private NodeLink addNodeLink(int treeid, int sourceid, int targetid) {
		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(sourceid);
		addTestNodeLink.setTargetId(targetid);
		return target("/ktrees/" + treeid + "/links")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class).readEntity(NodeLink.class);
	}

	private boolean deleteNodeLink(int nodeLinkId) {
		NodeLink deleteNodeLink = new NodeLink();
		deleteNodeLink.setId(nodeLinkId);
		return target("/ktrees/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteNodeLink, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Subnode addSubnode(int knid, String title) {
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setNodeId(knid);
		addTestSubnode.setTitle(title);
		return target("/ktrees/" + treeid + "/nodes/" + knid + "/subnodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Subnode.class);
	}

	private ChangesWrapper<?> deleteSubnode(int subnodeId, int knid) {
		Subnode deleteSubnode = new Subnode();
		deleteSubnode.setId(subnodeId);
		return target(
				"/ktrees/" + treeid + "/nodes/" + knid + "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteSubnode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(ChangesWrapper.class);
	}

	public Subnode getSubnode(int nodeId, int subnodeId) {
		Response getSubnodeResponse = target(
				"/ktrees/" + treeid + "/nodes/" + nodeId + "/subnodes/"
						+ subnodeId).request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		return getSubnodeResponse.readEntity(Subnode.class);
	}
}