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
import cdar.pl.controller.DirectoryController;
import cdar.pl.controller.KnowledgeNodeController;
import cdar.pl.controller.KnowledgeNodeLinkController;
import cdar.pl.controller.KnowledgeSubnodeController;
import cdar.pl.controller.KnowledgeTreeController;
import cdar.pl.controller.TemplateController;
import cdar.pl.controller.UserController;

public class TestTreeController extends JerseyTest {

	private final String USERNAME = "testuser";
	private final String PASSWORD = "testpassword";
	private final String TREENAME = "testtree";
	private final String UID = "uid";
	private final String ACCESSTOKEN = "accesstoken";
	private int userId;
	private int treeid;
	private String accesstoken;

	@Override
	protected Application configure() {
		return new ResourceConfig(KnowledgeTreeController.class,
				UserController.class, KnowledgeNodeController.class,
				TemplateController.class, DirectoryController.class,
				KnowledgeNodeLinkController.class,
				KnowledgeSubnodeController.class);
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
		Response quantityOfTreesBeforeAddRequest = target("ktree").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesBeforeAdd = quantityOfTreesBeforeAddRequest
				.readEntity(Set.class).size();

		Response createdTreeResponse = target("ktree/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(TREENAME, MediaType.APPLICATION_JSON),
						Response.class);
		Tree tree = createdTreeResponse.readEntity(Tree.class);
		treeid = tree.getId();
		int quantityOfTreesAfterAdd = target("/ktree").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		assertEquals(201, createdTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 1, quantityOfTreesAfterAdd);
	}

	@After
	public void testDeleteUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		int quantityOfTreesBeforeDelete = target("/ktree").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Response deletedTreeResponse = target("/ktree/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(treeid, MediaType.APPLICATION_JSON),
						Response.class);
		deletedTreeResponse.readEntity(boolean.class);
		int quantityOfTreesAfterDelete = target("/ktree").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();

		target("users/delete")
				.queryParam("password", PASSWORD)
				.queryParam("username", USERNAME)
				.request()
				.post(Entity.entity(userId, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(200, deletedTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeDelete - 1,
				quantityOfTreesAfterDelete);
	}

	@Test
	public void testDelteNotExistingTree() {
		Response deletedTreeResponse = target("/ktree/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTreeResponse.getStatus());
	}

	@Test
	public void testGetTree() {
		Response testTreeResponse = target("/ktree/" + treeid).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Tree testTree = testTreeResponse.readEntity(Tree.class);
		assertEquals(TREENAME, testTree.getTitle());
		assertEquals(200, testTreeResponse.getStatus());
	}

	@Test
	public void testGetAllTrees() {
		int quantityOfTreesBeforeAdd = target("/ktree").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree addedtree1 = addTree(TREENAME);
		Tree addedtree2 = addTree(TREENAME);

		Response quantityOfTreesAfterAddResponse = target("/ktree").request()
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
				"/ktree/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeId(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");

		Response addedTemplateResponse = target(
				"/ktree/" + treeid + "/templates/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Response.class);
		Template addedTemplate = addedTemplateResponse
				.readEntity(Template.class);

		int quantityOfTemplatesAfterAdd = target(
				"/ktree/" + treeid + "/templates/").request()
				.get(Response.class).readEntity(Set.class).size();

		Response deletedTemplateRequest = target(
				"/ktree/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedTemplate.getId(),
						MediaType.APPLICATION_JSON), Response.class);

		boolean isTemplateDeleted = deletedTemplateRequest
				.readEntity(boolean.class);

		int quantityOfTemplatesAfterDelete = target(
				"/ktree/" + treeid + "/templates/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		assertEquals(201, addedTemplateResponse.getStatus());
		assertEquals(200, deletedTemplateRequest.getStatus());
		assertEquals(treeid, addedTemplate.getTreeId());
		assertEquals("TestTemplate", addedTemplate.getTitle());
		assertEquals("TemplateText", addedTemplate.getTemplatetext());
		assertEquals(quantityOfTemplatesBeforeAdd + 1,
				quantityOfTemplatesAfterAdd);
		assertTrue(isTemplateDeleted);
		assertEquals(quantityOfTemplatesBeforeAdd,
				quantityOfTemplatesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingTemplate() {
		Response deletedTemplateResponse = target(
				"/ktree/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTemplateResponse.getStatus());
	}

	@Test
	public void testGetAllTemplates() {
		int quantityOfTemplatesBeforeAdd = target(
				"/ktree/" + treeid + "/templates").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();
		Template addedTemplate1 = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Template addedTemplate2 = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Response quantityOfTemplatesAfterAddResponse = target(
				"/ktree/" + treeid + "/templates").request()
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
	public void testGetTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Response getTemplateResponse = target(
				"/ktree/" + treeid + "/templates/" + addedTemplate.getId())
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		addedTemplate = getTemplateResponse.readEntity(Template.class);
		deleteTemplate(addedTemplate.getId());
		assertTrue(addedTemplate.getId() > 0);
		assertEquals(200, getTemplateResponse.getStatus());
	}

	@Test
	public void testEditTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		addedTemplate.setTemplatetext(TREENAME);
		addedTemplate.setTitle(USERNAME);
		Response editTemplateResponse = target(
				"/ktree/" + treeid + "/templates/edit")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
						Response.class);
		Template editTemplate = editTemplateResponse.readEntity(Template.class);
		deleteTemplate(addedTemplate.getId());
		assertEquals(200, editTemplateResponse.getStatus());
		assertEquals(TREENAME, editTemplate.getTemplatetext());
		assertEquals(USERNAME, editTemplate.getTitle());
	}

	@Test
	public void testAddAndDeleteDirectories() {
		int quantityOfDirectoriesBeforeAdd = target(
				"/ktree/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addDirectory = new Directory();
		addDirectory.setTreeId(treeid);
		addDirectory.setParentId(0);
		Response addedDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/add")
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
				"/ktree/" + treeid + "/directories/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addSubDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory addedSubDirectory = addedSubDirectoryResponse
				.readEntity(Directory.class);
		int quantityOfDirectoriesAfterAdd = target(
				"/ktree/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubDirectory.getId(),
						MediaType.APPLICATION_JSON), Response.class);
		boolean isSubDirectoryDeleted = deletedSubDirectoryResponse
				.readEntity(boolean.class);

		Response deletedDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedDirectory.getId(),
						MediaType.APPLICATION_JSON), Response.class);
		boolean isDirectoryDeleted = deletedDirectoryResponse
				.readEntity(boolean.class);

		int quantityOfDirectoriesAfterDelete = target(
				"/ktree/" + treeid + "/directories/").request()
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
		assertTrue(isSubDirectoryDeleted);
		assertTrue(isDirectoryDeleted);
	}

	@Test
	public void testDeleteNotExistingDirectory() {
		Response deletedDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedDirectoryResponse.getStatus());
	}

	@Test
	public void testGetAllDirectories() {
		int quantityOfDirectoriesBefore = target(
				"/ktree/" + treeid + "/directories").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addedDirectory1 = addDirectory(treeid, 0);
		Directory addedDirectory2 = addDirectory(treeid, 0);
		Response quantityOfDirectoriesAfterResponse = target(
				"/ktree/" + treeid + "/directories").request()
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
	public void testRenameDirectories() {
		Directory addedDirectory = addDirectory(treeid, 0);
		addedDirectory.setTitle(USERNAME);

		Response renamedDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/rename")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory renamedDirectory = renamedDirectoryResponse
				.readEntity(Directory.class);
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, renamedDirectoryResponse.getStatus());
		assertEquals(USERNAME, renamedDirectory.getTitle());
		assertEquals(treeid, renamedDirectory.getTreeId());
	}

	@Test
	public void testMovedDirectories() {
		Directory addedParentDirectory = addDirectory(treeid, 0);

		Directory addedChildDirectory = addDirectory(treeid, 0);
		addedChildDirectory.setParentId(addedParentDirectory.getId());

		Response movedDirectoryResponse = target(
				"/ktree/" + treeid + "/directories/move")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedChildDirectory,
						MediaType.APPLICATION_JSON), Response.class);
		Directory movedDirectory = movedDirectoryResponse
				.readEntity(Directory.class);
		deleteDirectory(addedChildDirectory.getId());
		deleteDirectory(addedParentDirectory.getId());
		assertEquals(200, movedDirectoryResponse.getStatus());
		assertEquals(addedParentDirectory.getId(), movedDirectory.getParentId());
	}

	@Test
	public void testAddAndDeleteNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target("/ktree/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(addedDirectory.getId());
		Response addedNodeResponse = target("/ktree/" + treeid + "/nodes/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class);
		Node addedNode = addedNodeResponse.readEntity(Node.class);

		int quantityOfNodesAfterAdd = target("/ktree/" + treeid + "/nodes")
				.request().get(Response.class).readEntity(Set.class).size();
		Response deletedNodeResponse = target(
				"/ktree/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNode.getId(),
						MediaType.APPLICATION_JSON), Response.class);
		int quantityOfNodesAfterDelete = target("/ktree/" + treeid + "/nodes")
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
		Response deletedNodeResponse = target(
				"/ktree/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedNodeResponse.getStatus());
	}

	@Test
	public void testGetAllNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target("/ktree/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		Response quantityOfNodesAfterAddResponse = target(
				"/ktree/" + treeid + "/nodes/").request().header(UID, userId)
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
	public void testGetNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Response receivedNodeResponse = target(
				"/ktree/treeid/nodes/" + addedNode.getId()).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, receivedNodeResponse.getStatus());
		assertEquals(treeid, addedNode.getTreeId());
		assertEquals(addedDirectory.getId(), addedNode.getDirectoryId());
	}

	@Test
	public void testAddAndDeleteNodeLink() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodeLinksBeforeAdd = target("/ktree/" + treeid + "/links")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(addedNode1.getId());
		addTestNodeLink.setTargetId(addedNode2.getId());
		Response addedNodeLinkResponse = target(
				"/ktree/" + treeid + "/links/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class);
		NodeLink addedNodeLink = addedNodeLinkResponse
				.readEntity(NodeLink.class);

		int quantityOfNodeLinksAfterAdd = target("/ktree/" + treeid + "/links/")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deleteNodeLinkResponse = target(
				"/ktree/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNodeLink.getId(),
						MediaType.APPLICATION_JSON), Response.class);

		int quantityOfNodeLinksAfterDelete = target(
				"/ktree/" + treeid + "/links").request().header(UID, userId)
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
		Response deleteNodeLinkResponse = target(
				"/ktree/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deleteNodeLinkResponse.getStatus());
	}

	@Test
	public void testGetAllNodeLinks() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		Response quantityOfNodeLinksBeforeAddResponse = target(
				"/ktree/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfNodeLinksBeforeAdd = quantityOfNodeLinksBeforeAddResponse
				.readEntity(Set.class).size();
		NodeLink addedNodeLink1 = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		NodeLink addedNodeLink2 = addNodeLink(treeid, addedNode2.getId(),
				addedNode1.getId());

		int quantityOfNodeLinksAfterAdd = target("/ktree/" + treeid + "/links/")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();

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
	// not working
	public void testUpdateNodeLinks() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		NodeLink addedNodeLink = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		Subnode addedSubnode = addSubnode(addedNode1.getId(), TREENAME);
		addedNodeLink.setSubnodeId(addedSubnode.getId());
		Response updatedNodeLinkResponse = target(
				"/ktree/" + treeid + "/links/update")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNodeLink, MediaType.APPLICATION_JSON),
						Response.class);
		NodeLink updatedNodeLink = updatedNodeLinkResponse
				.readEntity(NodeLink.class);
		deleteNodeLink(addedNodeLink.getId());

		deleteSubnode(addedSubnode.getId(), addedSubnode.getNodeId());

		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, updatedNodeLinkResponse.getStatus());
		assertEquals(addedSubnode.getId(), updatedNodeLink.getSubnodeId());
	}

	@Test
	public void testAddAndDeleteSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				"/ktree/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setNodeId(addedNode.getId());
		addTestSubnode.setTitle(TREENAME);
		Response addedSubnodeResponse = target(
				"/ktree/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class);
		Subnode addedSubnode = addedSubnodeResponse.readEntity(Subnode.class);

		int quantityOfSubnodesAfterAdd = target(
				"/ktree/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubnodeResponse = target(
				"/ktree/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubnode.getId(),
						MediaType.APPLICATION_JSON), Response.class);

		int quantityOfSubnodesAfterDelete = target(
				"/ktree/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		System.out.println(quantityOfSubnodesAfterDelete);
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
		Response deleteSubnodeResponse = target(
				"/ktree/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);

		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(400, deleteSubnodeResponse.getStatus());
	}

	@Test
	public void testGetAllSubnodes() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				"/ktree/" + treeid + "/subnodes").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		Response quantityOfSubnodesAfterAddResponse = target(
				"/ktree/" + treeid + "/subnodes").request().header(UID, userId)
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
	public void testGetSubnodesOfNode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		
		int quantityOfSubnodesBeforeAdd = target(
				"/ktree/" + treeid +"/nodes/"+addedNode.getId()+ "/subnodes").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);
		
		Response quantityOfSubnodesAfterAddResponse = target(
				"/ktree/" + treeid +"/nodes/"+addedNode.getId()+ "/subnodes").request().header(UID, userId)
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
	
	/*Not Implemented
	@Test
	public void testGetSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		
		Response queryAddedSubnodeResponse = target(
				"/ktree/" + treeid +"/nodes/"+addedNode.getId()+ "/subnodes/"+addedSubnode1.getId()).request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		Subnode queryAddedSubnode = queryAddedSubnodeResponse
				.readEntity(Subnode.class);
		
		deleteSubnode(addedSubnode1.getId(), addedNode.getId());
		
		deleteNode(addedNode.getId());
		
		deleteDirectory(addedDirectory.getId());
		assertNotEquals(null, queryAddedSubnode);
		assertEquals(200, queryAddedSubnodeResponse.getStatus());
	}*/

	private Tree addTree(String treename) {
		return target("/ktree/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(treename, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Tree.class);
	}

	private boolean deleteTree(int treeid) {
		return target("/ktree/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(treeid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Template addTemplate(int treeid, String title, String templatetext) {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeId(treeid);
		addTestTemplate.setTitle(title);
		addTestTemplate.setTemplatetext(templatetext);
		return target("/ktree/" + treeid + "/templates/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Template.class);
	}

	private boolean deleteTemplate(int templateid) {
		return target("/ktree/" + treeid + "/templates/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(templateid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	};

	private Directory addDirectory(int treeid, int parentid) {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setTreeId(treeid);
		addTestDirectory.setParentId(parentid);
		return target("/ktree/" + treeid + "/directories/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestDirectory,
						MediaType.APPLICATION_JSON), Response.class)
				.readEntity(Directory.class);
	}

	private boolean deleteDirectory(int directoryid) {
		return target("/ktree/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(directoryid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Node addNode(int treeid, int did) {
		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(did);
		return target("/ktree/" + treeid + "/nodes/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Node.class);
	}

	private boolean deleteNode(int nodeid) {
		return target("/ktree/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(nodeid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private NodeLink addNodeLink(int treeid, int sourceid, int targetid) {
		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(sourceid);
		addTestNodeLink.setTargetId(targetid);
		return target("/ktree/" + treeid + "/links/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class).readEntity(NodeLink.class);
	}

	private boolean deleteNodeLink(int nodeLinkId) {
		return target("/ktree/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(nodeLinkId, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Subnode addSubnode(int knid, String title) {
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setNodeId(knid);
		addTestSubnode.setTitle(title);
		return target("/ktree/" + treeid + "/nodes/" + knid + "/subnodes/add")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Subnode.class);
	}

	private ChangesWrapper<?> deleteSubnode(int subnodeId, int knid) {
		return target(
				"/ktree/" + treeid + "/nodes/" + knid + "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(subnodeId, MediaType.APPLICATION_JSON),
						Response.class).readEntity(ChangesWrapper.class);
	}

}
