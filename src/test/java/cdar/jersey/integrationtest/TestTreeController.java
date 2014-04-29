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

import cdar.bll.CDAR_Boolean;
import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
import cdar.bll.user.User;
import cdar.pl.controller.KnowledgeNodeController;
import cdar.pl.controller.KnowledgeNodeLinkController;
import cdar.pl.controller.KnowledgeSubnodeController;
import cdar.pl.controller.KnowledgeTreeController;
import cdar.pl.controller.UserController;

public class TestTreeController extends JerseyTest {

	private final String USERNAME = "testuser";
	private final String PASSWORD = "testpassword";
	private final String TREENAME = "testtree";
	private int userId;
	private int treeid;
	private String accesstoken;

	@Override
	protected Application configure() {
		return new ResourceConfig(KnowledgeTreeController.class,
				UserController.class, KnowledgeNodeController.class,
				KnowledgeNodeLinkController.class,
				KnowledgeSubnodeController.class);
	}

	private String getAuthString() {
		return String.format("%s/%d", accesstoken, userId);
	}

	@Before
	public void createUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user = target("users/registration")
				.request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON),
						Response.class).readEntity(User.class);
		user = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class)
				.readEntity(User.class);
		userId = user.getId();
		accesstoken = user.getAccesstoken();
		int quantityOfTreesBeforeAdd = target("/ktree")
				.request().get(Response.class).readEntity(Set.class).size();
		System.out.println(quantityOfTreesBeforeAdd);
		Response createdTreeResponse = target(
				getAuthString() + "/ktree/tree/add").request().post(
				Entity.entity(TREENAME, MediaType.APPLICATION_JSON),
				Response.class);
		Tree tree = createdTreeResponse.readEntity(Tree.class);

		treeid = tree.getId();

		int quantityOfTreesAfterAdd = target(getAuthString() + "/ktree")
				.request().get(Response.class).readEntity(Set.class).size();
		assertEquals(201, createdTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 1, quantityOfTreesAfterAdd);
	}

	@After
	public void testDeleteUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		int quantityOfTreesBeforeDelete = target(getAuthString() + "/ktree")
				.request().get(Response.class).readEntity(Set.class).size();

		Response deletedTreeResponse = target(getAuthString() + "/ktree/delete")
				.request().post(
						Entity.entity(treeid, MediaType.APPLICATION_JSON),
						Response.class);

		boolean isTreeDeleted = deletedTreeResponse.readEntity(boolean.class);

		int quantityOfTreesAfterDelete = target(getAuthString() + "/ktree")
				.request().get(Set.class).size();

		target("users/delete").request().post(
				Entity.entity(userId, MediaType.APPLICATION_JSON),
				CDAR_Boolean.class);

		assertEquals(200, deletedTreeResponse.getStatus());
		assertEquals(true, isTreeDeleted);
		assertEquals(quantityOfTreesBeforeDelete - 1,
				quantityOfTreesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingTree() {
		Response deletedTreeResponse = target(getAuthString() + "/ktree/delete")
				.request().post(
						Entity.entity(999999999, MediaType.APPLICATION_JSON),
						Response.class);		
		assertEquals(400, deletedTreeResponse.getStatus());
	}

	@Test
	public void testGetTree() {
		Tree testTree = target(getAuthString() + "/ktree/" + treeid).request()
				.get(Tree.class);
		assertEquals(TREENAME, testTree.getTitle());
		assertNotEquals(testTree.getId(), -1);
	}

	@Test
	public void testGetAllTrees() {
		int quantityOfTreesBeforeAdd = target(getAuthString() + "/ktree")
				.request().get(Set.class).size();
		Tree addedtree1 = addTree(TREENAME);
		Tree addedtree2 = addTree(TREENAME);

		int quantityOfTreesAfterAdd = target(getAuthString() + "/ktree")
				.request().get(Set.class).size();
		deleteTree(addedtree1.getId());
		deleteTree(addedtree2.getId());

		assertEquals(quantityOfTreesBeforeAdd + 2, quantityOfTreesAfterAdd);

	}

	@Test
	public void testAddAndDeleteTemplate() {
		int quantityOfTemplatesBeforeAdd = target(
				getAuthString() + "/ktree/templates/" + treeid).request()
				.get(Set.class).size();

		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");

		int quantityOfTemplatesAfterAdd = target(
				getAuthString() + "/ktree/templates/" + treeid).request()
				.get(Set.class).size();

		boolean isTemplateDeleted = deleteTemplate(addedTemplate.getId());

		int quantityOfTemplatesAfterDelete = target(
				getAuthString() + "/ktree/templates/" + treeid).request()
				.get(Set.class).size();
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
		boolean isTemplateDeleted = deleteTemplate(999999999);
		assertFalse(isTemplateDeleted);
	}

	@Test
	public void testGetAllTemplates() {
		int quantityOfTemplatesBeforeAdd = target(
				getAuthString() + "/ktree/templates/" + treeid).request()
				.get(Set.class).size();
		Template addedTemplate1 = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Template addedTemplate2 = addTemplate(treeid, "TestTemplate",
				"TemplateText");

		int quantityOfTemplateAfterAdd = target(
				getAuthString() + "/ktree/templates/" + treeid).request()
				.get(Set.class).size();
		deleteTemplate(addedTemplate1.getId());
		deleteTemplate(addedTemplate2.getId());

		assertEquals(quantityOfTemplatesBeforeAdd + 2,
				quantityOfTemplateAfterAdd);

	}

	@Test
	public void testGetTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		Template getTemplate = target(
				getAuthString() + "/ktree/templates/" + treeid + "/"
						+ addedTemplate.getId()).request().get(Template.class);
		deleteTemplate(addedTemplate.getId());
		assertNotEquals(-1, getTemplate.getId());
	}

	@Test
	public void testEditTemplate() {
		Template addedTemplate = addTemplate(treeid, "TestTemplate",
				"TemplateText");
		addedTemplate.setTemplatetext(TREENAME);
		addedTemplate.setTitle(USERNAME);
		Template editTemplate = target(
				getAuthString() + "/ktree/templates/edit/"
						+ addedTemplate.getId()).request().post(
				Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
				Template.class);
		deleteTemplate(addedTemplate.getId());
		assertEquals(TREENAME, editTemplate.getTemplatetext());
		assertEquals(USERNAME, editTemplate.getTitle());
	}

	@Test
	public void testAddAndDeleteDirectories() {
		int quantityOfDirectoriesBeforeAdd = target(
				getAuthString() + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();
		Directory addedDirectory = addDirectory(treeid, 0);
		Directory addedSubDirectory = addDirectory(treeid,
				addedDirectory.getId());

		int quantityOfDirectoriesAfterAdd = target(
				getAuthString() + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();

		Boolean isDirectoryDeleted = deleteDirectory(addedDirectory.getId());
		deleteDirectory(addedSubDirectory.getId());

		int quantityOfDirectoriesAfterDelete = target(
				getAuthString() + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();
		assertEquals(treeid, addedDirectory.getKtrid());
		assertEquals(0, addedDirectory.getParentid());
		assertEquals(addedDirectory.getId(), addedSubDirectory.getParentid());

		assertEquals(quantityOfDirectoriesBeforeAdd + 2,
				quantityOfDirectoriesAfterAdd);
		assertEquals(quantityOfDirectoriesBeforeAdd,
				quantityOfDirectoriesAfterDelete);
		assertTrue(isDirectoryDeleted);
	}

	@Test
	public void testDeleteNotExistingDirectory() {
		boolean isDirectoryDeleted = deleteDirectory(999999999);
		assertEquals(false, isDirectoryDeleted);
	}

	@Test
	public void testGetAllDirectories() {
		int quantityOfDirectoriesBefore = target(
				getAuthString() + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();

		Directory addedDirectory1 = addDirectory(treeid, 0);
		Directory addedDirectory2 = addDirectory(treeid, 0);

		int quantityOfDirectoriesAfter = target(
				getAuthString() + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();

		deleteDirectory(addedDirectory1.getId());
		deleteDirectory(addedDirectory2.getId());
		assertEquals(quantityOfDirectoriesBefore + 2,
				quantityOfDirectoriesAfter);
	}

	@Test
	public void testRenameDirectories() {
		Directory addedDirectory = addDirectory(treeid, 0);
		addedDirectory.setTitle(USERNAME);

		Directory renamedDirectory = target(
				getAuthString() + "/ktree/directories/rename/" + treeid)
				.request().post(
						Entity.entity(addedDirectory,
								MediaType.APPLICATION_JSON), Directory.class);

		deleteDirectory(addedDirectory.getId());
		assertEquals(USERNAME, renamedDirectory.getTitle());
		assertEquals(treeid, renamedDirectory.getKtrid());
	}

	@Test
	public void testMovedDirectories() {
		Directory addedParentDirectory = addDirectory(treeid, 0);

		Directory addedChildDirectory = addDirectory(treeid, 0);
		addedChildDirectory.setParentid(addedParentDirectory.getId());

		Directory movedDirectory = target(
				getAuthString() + "/ktree/directories/move/" + treeid)
				.request().post(
						Entity.entity(addedChildDirectory,
								MediaType.APPLICATION_JSON), Directory.class);
		deleteDirectory(addedParentDirectory.getId());
		deleteDirectory(addedChildDirectory.getId());

		assertEquals(addedParentDirectory.getId(), movedDirectory.getParentid());
	}

	@Test
	public void testAddAndDeleteNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfNodesAfterAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		boolean isNodeDeleted = deleteNode(addedNode.getId());

		int quantityOfNodesAfterDelete = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();
		deleteDirectory(addedDirectory.getId());

		assertEquals(treeid, addedNode.getKtrid());
		assertEquals(addedDirectory.getId(), addedNode.getDid());
		assertTrue(isNodeDeleted);
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterAdd);
		assertEquals(quantityOfNodesBeforeAdd, quantityOfNodesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingNode() {
		boolean isNodeDeleted = deleteNode(999999999);
		assertFalse(isNodeDeleted);
	}

	@Test
	public void testGetAllNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodesAfterAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(quantityOfNodesBeforeAdd + 2, quantityOfNodesAfterAdd);
	}

	@Test
	public void testGetNodes() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Node receivedNode = target(
				getAuthString() + "/ktree/nodes/" + treeid + "/"
						+ addedNode.getId()).request().get(Node.class);
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(treeid, addedNode.getKtrid());
		assertEquals(addedDirectory.getId(), addedNode.getDid());
		assertNotEquals(-1, receivedNode.getId());
	}

	@Test
	public void testDropNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		Node addedNode = addNode(treeid, addedDirectory.getId());
		Node droppedNode = target(
				getAuthString() + "/ktree/nodes/drop/" + treeid).request()
				.post(Entity.entity(addedNode.getId(),
						MediaType.APPLICATION_JSON), Node.class);

		int quantityOfNodesAfterDrop = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(1, droppedNode.getDynamicTreeFlag());
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterDrop);
	}

	@Test
	public void testUndropNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		Node addedNode = addNode(treeid, addedDirectory.getId());

		addedNode = target(getAuthString() + "/ktree/nodes/drop/" + treeid)
				.request().post(
						Entity.entity(addedNode.getId(),
								MediaType.APPLICATION_JSON), Node.class);
		Node undroppedNode = target(
				getAuthString() + "/ktree/nodes/undrop/" + treeid).request()
				.post(Entity.entity(addedNode.getId(),
						MediaType.APPLICATION_JSON), Node.class);

		int quantityOfNodesAfterDrop = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(1, addedNode.getDynamicTreeFlag());
		assertEquals(0, undroppedNode.getDynamicTreeFlag());
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterDrop);
	}

	@Test
	public void testRenameNode() {
		Directory addedDirectory = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();

		Node addedNode = addNode(treeid, addedDirectory.getId());
		addedNode.setTitle(USERNAME);

		Node renamedNode = target(
				getAuthString() + "/ktree/nodes/rename/" + treeid).request()
				.post(Entity.entity(addedNode, MediaType.APPLICATION_JSON),
						Node.class);

		int quantityOfNodesAfterDrop = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(addedNode.getId(), renamedNode.getId());
		assertEquals(USERNAME, renamedNode.getTitle());
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterDrop);
	}

	@Test
	public void testMoveNode() {
		Directory addedDirectory1 = addDirectory(treeid, 0);
		Directory addedDirectory2 = addDirectory(treeid, 0);

		int quantityOfNodesBeforeAdd = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();
		Node addedNode = addNode(treeid, addedDirectory1.getId());
		addedNode.setDid(addedDirectory2.getId());
		Node movedNode = target(getAuthString() + "/ktree/nodes/move/" + treeid)
				.request().post(
						Entity.entity(addedNode, MediaType.APPLICATION_JSON),
						Node.class);
		int quantityOfNodesAfterDrop = target(
				getAuthString() + "/ktree/nodes/" + treeid).request()
				.get(Set.class).size();
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory1.getId());
		deleteDirectory(addedDirectory2.getId());
		assertEquals(addedDirectory2.getId(), movedNode.getDid());
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterDrop);
	}

	@Test
	public void testAddAndDeleteNodeLink() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodeLinksBeforeAdd = target(
				getAuthString() + "/ktree/links/" + treeid).request()
				.get(Set.class).size();
		NodeLink addedNodeLink = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());

		int quantityOfNodeLinksAfterAdd = target(
				getAuthString() + "/ktree/links/" + treeid).request()
				.get(Set.class).size();

		boolean isNodeLinkDeleted = deleteNodeLink(addedNodeLink.getId());

		int quantityOfNodeLinksAfterDelete = target(
				getAuthString() + "/ktree/links/" + treeid).request()
				.get(Set.class).size();
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(treeid, addedNodeLink.getKtrid());
		assertEquals(addedNode1.getId(), addedNodeLink.getSourceId());
		assertEquals(addedNode2.getId(), addedNodeLink.getTargetId());
		assertTrue(isNodeLinkDeleted);
		assertEquals(quantityOfNodeLinksBeforeAdd + 1,
				quantityOfNodeLinksAfterAdd);
		assertEquals(quantityOfNodeLinksBeforeAdd,
				quantityOfNodeLinksAfterDelete);
	}

	@Test
	public void testDeleteNotExistingNodeLink() {
		boolean isNodeLinkDeleted = deleteNodeLink(999999999);
		assertFalse(isNodeLinkDeleted);
	}

	@Test
	public void testGetAllNodeLinks() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodeLinksBeforeAdd = target(
				getAuthString() + "/ktree/links/" + treeid).request()
				.get(Set.class).size();
		NodeLink addedNodeLink1 = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		NodeLink addedNodeLink2 = addNodeLink(treeid, addedNode2.getId(),
				addedNode1.getId());

		int quantityOfNodeLinksAfterAdd = target(
				getAuthString() + "/ktree/links/" + treeid).request()
				.get(Set.class).size();

		deleteNodeLink(addedNodeLink1.getId());
		deleteNodeLink(addedNodeLink2.getId());
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());

		assertEquals(quantityOfNodeLinksBeforeAdd + 2,
				quantityOfNodeLinksAfterAdd);
	}

	@Test
	public void testUpdateNodeLinks() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		NodeLink addedNodeLink = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		Subnode addedSubnode = addSubnode(addedNode1.getId(), TREENAME);
		addedNodeLink.setKsnid(addedSubnode.getId());
		NodeLink updatedNodeLink = target(
				getAuthString() + "/ktree/links/update/" + treeid).request()
				.post(Entity.entity(addedNodeLink, MediaType.APPLICATION_JSON),
						NodeLink.class);
		deleteSubnode(addedSubnode.getId());
		deleteNodeLink(addedNodeLink.getId());
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(addedSubnode.getId(), updatedNodeLink.getKsnid());
	}

	@Test
	public void testAddAndDeleteSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid).request()
				.get(Set.class).size();
		Subnode addedSubnode = addSubnode(addedNode.getId(), TREENAME);

		int quantityOfSubnodesAfterAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid).request()
				.get(Set.class).size();

		boolean isSubnodeDeleted = deleteSubnode(addedSubnode.getId());

		int quantityOfSubnodesAfterDelete = target(
				getAuthString() + "/ktree/subnodes/" + treeid).request()
				.get(Set.class).size();
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(addedNode.getId(), addedSubnode.getKnid());
		assertEquals(TREENAME, addedSubnode.getTitle());
		assertTrue(isSubnodeDeleted);
		assertEquals(quantityOfSubnodesBeforeAdd + 1,
				quantityOfSubnodesAfterAdd);
		assertEquals(quantityOfSubnodesBeforeAdd, quantityOfSubnodesAfterDelete);
	}

	@Test
	public void testDeleteNonExistingSubnode() {
		boolean isSubnodeDeleted = deleteSubnode(999999999);
		assertFalse(isSubnodeDeleted);
	}

	@Test
	public void testGetAllSubnodes() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid).request()
				.get(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		int quantityOfSubnodesAfterAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid).request()
				.get(Set.class).size();
		deleteSubnode(addedSubnode1.getId());
		deleteSubnode(addedSubnode2.getId());

		deleteNode(addedNode.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(quantityOfSubnodesBeforeAdd + 2,
				quantityOfSubnodesAfterAdd);
	}

	@Test
	public void testGetSubnode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());

		int quantityOfSubnodesBeforeAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid + "/"
						+ addedNode.getId()).request().get(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		int quantityOfSubnodesAfterAdd = target(
				getAuthString() + "/ktree/subnodes/" + treeid + "/"
						+ addedNode.getId()).request().get(Set.class).size();
		deleteSubnode(addedSubnode1.getId());
		deleteSubnode(addedSubnode2.getId());

		deleteNode(addedNode.getId());

		deleteDirectory(addedDirectory.getId());
		assertEquals(quantityOfSubnodesBeforeAdd + 2,
				quantityOfSubnodesAfterAdd);
	}

	private Tree addTree(String treename) {
		return target(getAuthString() + "/ktree/tree/add")
				.request()
				.post(Entity.entity(treename, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Tree.class);
	}

	private boolean deleteTree(int treeid) {
		return target(getAuthString() + "/ktree/delete")
				.request()
				.post(Entity.entity(treeid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Template addTemplate(int treeid, String title, String templatetext) {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeId(treeid);
		addTestTemplate.setTitle(title);
		addTestTemplate.setTemplatetext(templatetext);
		return target(getAuthString() + "/ktree/templates/add/" + treeid)
				.request()
				.post(Entity
						.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Template.class);
	}

	private boolean deleteTemplate(int templateid) {
		return target(getAuthString() + "/ktree/templates/delete/" + treeid)
				.request()
				.post(Entity.entity(templateid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	};

	private Directory addDirectory(int treeid, int parentid) {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(parentid);
		return target(getAuthString() + "/ktree/directories/add/" + treeid)
				.request()
				.post(Entity.entity(addTestDirectory,
						MediaType.APPLICATION_JSON), Response.class)
				.readEntity(Directory.class);
	}

	private boolean deleteDirectory(int directoryid) {
		return target(getAuthString() + "/ktree/directories/delete/" + treeid)
				.request()
				.post(Entity.entity(directoryid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Node addNode(int treeid, int did) {
		Node addTestNode = new Node();
		addTestNode.setKtrid(treeid);
		addTestNode.setDid(did);
		return target(getAuthString() + "/ktree/nodes/add/" + "" + treeid)
				.request()
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Node.class);
	}

	private boolean deleteNode(int nodeid) {
		return target(getAuthString() + "/ktree/nodes/delete/" + treeid)
				.request()
				.post(Entity.entity(nodeid, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private NodeLink addNodeLink(int treeid, int sourceid, int targetid) {
		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setKtrid(treeid);
		addTestNodeLink.setSourceId(sourceid);
		addTestNodeLink.setTargetId(targetid);
		return target(getAuthString() + "/ktree/links/add/" + "" + treeid)
				.request()
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class).readEntity(NodeLink.class);
	}

	private boolean deleteNodeLink(int nodeLinkId) {
		return target(getAuthString() + "/ktree/links/delete/" + treeid)
				.request()
				.post(Entity.entity(nodeLinkId, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Subnode addSubnode(int knid, String title) {
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setKnid(knid);
		addTestSubnode.setTitle(title);
		return target(getAuthString() + "/ktree/subnodes/add/" + "" + treeid)
				.request()
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Subnode.class);
	}

	private boolean deleteSubnode(int subnodeId) {
		return target(getAuthString() + "/ktree/subnodes/delete/" + treeid)
				.request()
				.post(Entity.entity(subnodeId, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

}
