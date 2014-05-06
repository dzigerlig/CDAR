package cdar.jersey.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
import cdar.bll.entity.consumer.Comment;
import cdar.pl.controller.UserController;
import cdar.pl.controller.consumer.CommentController;
import cdar.pl.controller.consumer.ProjectDirectoryController;
import cdar.pl.controller.consumer.ProjectNodeController;
import cdar.pl.controller.consumer.ProjectNodeLinkController;
import cdar.pl.controller.consumer.ProjectSubnodeController;
import cdar.pl.controller.consumer.ProjectTreeController;

public class TestProjectTreeController extends JerseyTest {
	private final String USERNAME = "testuser";
	private final String PASSWORD = "testpassword";
	private final String TREENAME = "testtree";
	private final String TITLE = "noname";
	private final String UID = "uid";
	private final String ACCESSTOKEN = "accesstoken";
	private final int NOTEXISTINGID = 999999999;
	private int userId;
	private int treeid;
	private String accesstoken;

	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class,
				ProjectTreeController.class, ProjectDirectoryController.class,
				ProjectNodeController.class, ProjectNodeLinkController.class, ProjectSubnodeController.class, CommentController.class);
	}

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
		Response quantityOfTreesBeforeAddRequest = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesBeforeAdd = quantityOfTreesBeforeAddRequest
				.readEntity(Set.class).size();
		Tree addTree = new Tree();
		addTree.setTitle(TREENAME);
		Response createdTreeResponse = target("ptrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree tree = createdTreeResponse.readEntity(Tree.class);
		treeid = tree.getId();
		int quantityOfTreesAfterAdd = target("ptrees").request()
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
		int quantityOfTreesBeforeDelete = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		Response deletedTreeResponse = target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfTreesAfterDelete = target("ptrees").request()
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
		Response deletedTreeResponse = target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTreeResponse.getStatus());
	}

	@Test
	public void testGetTree() {
		Response testTreeResponse = target("ptrees/" + treeid).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Tree testTree = testTreeResponse.readEntity(Tree.class);
		assertEquals(TREENAME, testTree.getTitle());
		assertEquals(200, testTreeResponse.getStatus());
	}

	@Test
	public void testEditTree() {
		Tree editTree = new Tree();
		editTree.setTitle("editTree");
		editTree.setId(treeid);
		Response editedTreeResponse = target("ptrees/" + treeid)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(editTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree editedTree = getTree(treeid);
		assertEquals(200, editedTreeResponse.getStatus());
		assertEquals("editTree", editedTree.getTitle());
	}

	@Test
	public void testGetAllTrees() {
		int quantityOfTreesBeforeAdd = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree addedtree1 = addTree(TREENAME);
		Tree addedtree2 = addTree(TREENAME);

		Response quantityOfTreesAfterAddResponse = target("ptrees").request()
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
	public void testAddAndDeleteDirectories() {
		int quantityOfDirectoriesBeforeAdd = target(
				"ptrees/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addDirectory = new Directory();
		addDirectory.setTreeId(treeid);
		addDirectory.setParentId(0);
		Response addedDirectoryResponse = target(
				"ptrees/" + treeid + "/directories")
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
				"ptrees/" + treeid + "/directories")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addSubDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		Directory addedSubDirectory = addedSubDirectoryResponse
				.readEntity(Directory.class);
		int quantityOfDirectoriesAfterAdd = target(
				"ptrees/" + treeid + "/directories/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubDirectoryResponse = target(
				"ptrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubDirectory,
						MediaType.APPLICATION_JSON), Response.class);
		Response deletedDirectoryResponse = target(
				"ptrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedDirectory, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfDirectoriesAfterDelete = target(
				"ptrees/" + treeid + "/directories/").request()
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
				"ptrees/" + treeid + "/directories/delete")
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
				"ptrees/" + treeid + "/directories/" + addedDirectory1.getId())
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
				"ptrees/" + treeid + "/directories").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Directory addedDirectory1 = addDirectory(treeid, 0);
		Directory addedDirectory2 = addDirectory(treeid, 0);
		Response quantityOfDirectoriesAfterResponse = target(
				"ptrees/" + treeid + "/directories").request()
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
		int addedDirectoryId = addedDirectory.getId();
		addedDirectory.setId(0);
		Response updatedDirectoryResponse = target(
				"ptrees/" + treeid + "/directories/" + addedDirectoryId)
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

		int quantityOfNodesBeforeAdd = target("ptrees/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(addedDirectory.getId());
		addTestNode.setTitle(TITLE);
		Response addedNodeResponse = target("ptrees/" + treeid + "/nodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class);
		Node addedNode = addedNodeResponse.readEntity(Node.class);

		int quantityOfNodesAfterAdd = target("ptrees/" + treeid + "/nodes")
				.request().get(Response.class).readEntity(Set.class).size();
		Response deletedNodeResponse = target(
				"ptrees/" + treeid + "/nodes/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNode, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfNodesAfterDelete = target("ptrees/" + treeid + "/nodes")
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
				"ptrees/" + treeid + "/nodes/delete/")
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
				"ptrees/treeid/nodes/" + addedNode.getId()).request()
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

		int quantityOfNodesBeforeAdd = target("ptrees/" + treeid + "/nodes")
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		Response quantityOfNodesAfterAddResponse = target(
				"ptrees/" + treeid + "/nodes/").request().header(UID, userId)
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
		int nodeId = addedNode.getId();
		addedNode.setId(0);
		Response updatedNodeResponse = target(
				"ptrees/" + treeid + "/nodes/" + nodeId)
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
		assertEquals(addedDirectory.getId(), updatedNode.getDirectoryId());
	}
	
	@Test
	public void testAddAndDeleteNodeLink() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());

		int quantityOfNodeLinksBeforeAdd = target(
				"ptrees/" + treeid + "/links").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();

		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(addedNode1.getId());
		addTestNodeLink.setTargetId(addedNode2.getId());
		Response addedNodeLinkResponse = target("ptrees/" + treeid + "/links")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(addTestNodeLink, MediaType.APPLICATION_JSON),
						Response.class);
		NodeLink addedNodeLink = addedNodeLinkResponse
				.readEntity(NodeLink.class);

		int quantityOfNodeLinksAfterAdd = target(
				"ptrees/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();

		Response deleteNodeLinkResponse = target(
				"ptrees/" + treeid + "/links/delete/")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedNodeLink, MediaType.APPLICATION_JSON),
						Response.class);

		int quantityOfNodeLinksAfterDelete = target(
				"ptrees/" + treeid + "/links").request().header(UID, userId)
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
				"ptrees/" + treeid + "/links/delete")
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
				"ptrees/" + treeid + "/links/").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfNodeLinksBeforeAdd = quantityOfNodeLinksBeforeAddResponse
				.readEntity(Set.class).size();
		NodeLink addedNodeLink1 = addNodeLink(treeid, addedNode1.getId(),
				addedNode2.getId());
		NodeLink addedNodeLink2 = addNodeLink(treeid, addedNode2.getId(),
				addedNode1.getId());

		int quantityOfNodeLinksAfterAdd = target(
				"ptrees/" + treeid + "/links/").request().header(UID, userId)
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
				"ptrees/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Subnode addTestSubnode = new Subnode();
		addTestSubnode.setNodeId(addedNode.getId());
		addTestSubnode.setTitle(TREENAME);
		Response addedSubnodeResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestSubnode, MediaType.APPLICATION_JSON),
						Response.class);
		Subnode addedSubnode = addedSubnodeResponse.readEntity(Subnode.class);

		int quantityOfSubnodesAfterAdd = target(
				"ptrees/" + treeid + "/subnodes/").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();

		Response deletedSubnodeResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedSubnode, MediaType.APPLICATION_JSON),
						Response.class);

		int quantityOfSubnodesAfterDelete = target(
				"ptrees/" + treeid + "/subnodes/").request()
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
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
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
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
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
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
						+ "/subnodes").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		Response quantityOfSubnodesAfterAddResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
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
				"ptrees/" + treeid + "/subnodes").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Subnode addedSubnode1 = addSubnode(addedNode.getId(), PASSWORD);
		Subnode addedSubnode2 = addSubnode(addedNode.getId(), USERNAME);

		Response quantityOfSubnodesAfterAddResponse = target(
				"ptrees/" + treeid + "/subnodes").request()
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
				"ptrees/" + treeid + "/nodes/" + addedNode1.getId()
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
	public void testAddAndDeleteComment() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		
		int quantityOfCommentsBeforeAdd = target(
				"ptrees/" + treeid + "/nodes/"+addedNode.getId()+"/comments").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Comment addTestComment = new Comment();
		addTestComment.setNodeId(addedNode.getId());
		addTestComment.setComment(TREENAME);
		addTestComment.setUserId(userId);
		Response addedCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestComment, MediaType.APPLICATION_JSON),
						Response.class);
		Comment addedComment = addedCommentResponse.readEntity(Comment.class);
		
		int quantityOfCommentsAfterAdd = target(
				"ptrees/" + treeid + "/nodes/"+addedNode.getId()+"/comments").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		
		Response deletedCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedComment, MediaType.APPLICATION_JSON),
						Response.class);
		
		int quantityOfCommentsAfterDelete = target(
				"ptrees/" + treeid + "/nodes/"+addedNode.getId()+"/comments").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, deletedCommentResponse.getStatus());
		assertEquals(201, addedCommentResponse.getStatus());
		assertEquals(addedNode.getId(), addedComment.getNodeId());
		assertEquals(TREENAME, addedComment.getComment());
		assertEquals(quantityOfCommentsBeforeAdd + 1,
				quantityOfCommentsAfterAdd);
		assertEquals(quantityOfCommentsBeforeAdd, quantityOfCommentsAfterDelete);
	}
	
	@Test
	public void testDeleteNonExistingComment() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		Comment deleteComment = new Comment();
		deleteComment.setId(NOTEXISTINGID);
		Response deleteCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteComment, MediaType.APPLICATION_JSON),
						Response.class);
		
		deleteNode(addedNode.getId());
		deleteDirectory(addedDirectory.getId());
		assertEquals(400, deleteCommentResponse.getStatus());
	}
	
	@Test
	public void testGetComment() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		
		Comment addedComment = addComment(addedNode.getId(), PASSWORD);
		
		Response getCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments/" + addedComment.getId()).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Comment getComment = getCommentResponse.readEntity(Comment.class);
		deleteComment(addedComment.getId(), addedNode.getId());
		
		deleteNode(addedNode.getId());
		
		deleteDirectory(addedDirectory.getId());
		assertNotEquals(null, getComment);
		assertEquals(200, getCommentResponse.getStatus());
	}
	
	@Test
	public void testGetCommentsOfNode() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode = addNode(treeid, addedDirectory.getId());
		
		int quantityOfCommentsBeforeAdd = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class)
				.readEntity(Set.class).size();
		Comment addedComment1 = addComment(addedNode.getId(), PASSWORD);
		Comment addedComment2 = addComment(addedNode.getId(), USERNAME);
		
		Response quantityOfCommentsAfterAddResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode.getId()
				+ "/comments").request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		int quantityOfCommentsAfterAdd = quantityOfCommentsAfterAddResponse
				.readEntity(Set.class).size();
		
		deleteComment(addedComment1.getId(), addedNode.getId());
		deleteComment(addedComment2.getId(), addedNode.getId());
		
		deleteNode(addedNode.getId());
		
		deleteDirectory(addedDirectory.getId());
		assertEquals(200, quantityOfCommentsAfterAddResponse.getStatus());
		assertEquals(quantityOfCommentsBeforeAdd + 2,
				quantityOfCommentsAfterAdd);
	}
	
	@Test
	public void testEditComment() {
		Directory addedDirectory = addDirectory(treeid, 0);
		Node addedNode1 = addNode(treeid, addedDirectory.getId());
		Node addedNode2 = addNode(treeid, addedDirectory.getId());
		Comment addedComment = addComment(addedNode1.getId(), PASSWORD);
		addedComment.setComment(USERNAME);
		int commentId = addedComment.getId();
		addedComment.setId(0);
		Response updatedCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + addedNode1.getId()
				+ "/comments/" + commentId)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addedComment, MediaType.APPLICATION_JSON),
						Response.class);
		Comment updatedComment = getComment(addedNode1.getId(), commentId);
		deleteComment(commentId, addedNode1.getId());
		
		deleteNode(addedNode1.getId());
		deleteNode(addedNode2.getId());
		
		deleteDirectory(addedDirectory.getId());
		assertNotEquals(null, updatedComment);
		assertEquals(USERNAME, updatedComment.getComment());
		assertEquals(200, updatedCommentResponse.getStatus());
	}
	

	private Tree addTree(String treename) {
		Tree addTree = new Tree();
		addTree.setTitle(treename);
		return target("ptrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Tree.class);
	}

	private boolean deleteTree(int treeid) {
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		return target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Tree getTree(int id) {
		Response testTreeResponse = target("ptrees/" + id).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		return testTreeResponse.readEntity(Tree.class);
	}

	private Directory addDirectory(int treeid, int parentid) {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setTreeId(treeid);
		addTestDirectory.setParentId(parentid);
		return target("ptrees/" + treeid + "/directories")
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
		return target("ptrees/" + treeid + "/directories/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity
						.entity(deleteDirectory, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Directory getDirectory(int directroyId) {
		return target("ptrees/" + treeid + "/directories/" + directroyId)
				.request().header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Directory.class);
	}

	private Node addNode(int treeid, int did) {
		Node addTestNode = new Node();
		addTestNode.setTreeId(treeid);
		addTestNode.setDirectoryId(did);
		addTestNode.setTitle(TITLE);
		return target("ptrees/" + treeid + "/nodes")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Node.class);
	}

	private boolean deleteNode(int nodeid) {
		Node deleteNode = new Node();
		deleteNode.setId(nodeid);
		return target("ptrees/" + treeid + "/nodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteNode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Node getNode(int nodeId) {
		return target("ptrees/treeid/nodes/" + nodeId).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Node.class);
	}
	
	private NodeLink addNodeLink(int treeid, int sourceid, int targetid) {
		NodeLink addTestNodeLink = new NodeLink();
		addTestNodeLink.setTreeId(treeid);
		addTestNodeLink.setSourceId(sourceid);
		addTestNodeLink.setTargetId(targetid);
		return target("ptrees/" + treeid + "/links")
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
		return target("ptrees/" + treeid + "/links/delete/")
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
		return target("ptrees/" + treeid + "/nodes/" + knid + "/subnodes")
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
				"ptrees/" + treeid + "/nodes/" + knid + "/subnodes/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteSubnode, MediaType.APPLICATION_JSON),
						Response.class).readEntity(ChangesWrapper.class);
	}

	public Subnode getSubnode(int nodeId, int subnodeId) {
		Response getSubnodeResponse = target(
				"ptrees/" + treeid + "/nodes/" + nodeId + "/subnodes/"
						+ subnodeId).request().header(UID, userId)
				.header(ACCESSTOKEN, accesstoken).get(Response.class);
		return getSubnodeResponse.readEntity(Subnode.class);
	}
	
	private Comment addComment(int knid, String text) {
		Comment addTestComment = new Comment();
		addTestComment.setNodeId(knid);
		addTestComment.setComment(text);
		addTestComment.setUserId(userId);
		return target("ptrees/" + treeid + "/nodes/" + knid + "/comments")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTestComment, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Comment.class);
	}
	
	private boolean deleteComment(int commentid, int knid) {
		Comment deleteComment = new Comment();
		deleteComment.setId(commentid);
		return target(
				"ptrees/" + treeid + "/nodes/" + knid + "/comments/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteComment, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}
	
	public Comment getComment(int nodeId, int commentId) {
		Response getCommentResponse = target(
				"ptrees/" + treeid + "/nodes/" + nodeId + "/comments/"
						+ commentId).request().header(UID, userId)
						.header(ACCESSTOKEN, accesstoken).get(Response.class);
		return getCommentResponse.readEntity(Comment.class);
	}
}
