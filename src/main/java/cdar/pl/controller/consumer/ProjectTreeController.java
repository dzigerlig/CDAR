package cdar.pl.controller.consumer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.UserRole;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.entity.consumer.CreationTree;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.pl.controller.StatusHelper;
import cdar.pl.controller.UserController;

@Path("ptrees")
public class ProjectTreeController {
	private final boolean ISPRODUCER = false;
	private LockingManager lm = new LockingManager();
	private TreeManager ptm = new TreeManager(UserRole.CONSUMER);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreesByUid(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getTrees(uid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectTree(CreationTree tree,
			@HeaderParam("uid") int uid) {
		try {
			int knowledgeTreeId = tree.getCopyTreeId();
			tree.setUserId(uid);
			Tree newTree = ptm.addTree(uid, tree);
			ptm.addKnowledgeTreeToProjectTree(knowledgeTreeId, newTree.getId());
			return StatusHelper.getStatusCreated(newTree);
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreeById(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getTree(treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProjectTree(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Tree tree) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			tree.setId(treeId);
			return StatusHelper.getStatusOk(ptm.updateTree(tree));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTree(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Tree tree) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			ptm.deleteTree(tree.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{ptreeid}/subnodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("ptreeid") int treeId) {
		try {
			ProjectSubnodeManager psm = new ProjectSubnodeManager();
			return StatusHelper.getStatusOk(psm
					.getProjectSubnodesFromProjectTree(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{ptreeid}/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithTreeRight(@PathParam("ptreeid") int treeId) {
		try {
			UserManager um = new UserManager();
			List<User> userList = um.getUsersByTree(false, treeId);
			for (User user : um.getUsers()) {
				if (!userList.contains(user)) {
					userList.add(user);
				}
			}
			for (User user : userList) {
				user.setPassword("");
				user.setAccesstoken("");
			}
			return StatusHelper.getStatusOk(userList);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{ptreeid}/users/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setUserRight(@PathParam("ptreeid") int treeId,
			@PathParam("uid") int userId, User user) {
		try {
			user.setId(userId);
			UserManager um = new UserManager();
			um.setConsumerUserRight(treeId, user);
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
