package ch.cdar.pl.controller.consumer;

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

import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.consumer.CreationTree;
import ch.cdar.bll.exceptions.LockingException;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.bll.manager.consumer.ProjectSubnodeManager;
import ch.cdar.pl.StatusHelper;

/**
 * The Class ProjectTreeController.
 */
@Path("ptrees")
public class ProjectTreeController {
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The locking manager. */
	private LockingManager lm = new LockingManager();
	
	/** The project tree m anager. */
	private TreeManager ptm = new TreeManager(UserRole.CONSUMER);

	/**
	 * Gets the project trees by uid.
	 *
	 * @param uid the uid
	 * @return the project trees by uid
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreesByUid(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getTrees(uid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the project tree.
	 *
	 * @param tree the tree
	 * @param uid the uid
	 * @return the response
	 */
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

	/**
	 * Gets the project tree by id.
	 *
	 * @param treeId the tree id
	 * @param uid the uid
	 * @return the project tree by id
	 */
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

	/**
	 * Update project tree.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param tree the tree
	 * @return the response
	 */
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
			System.out.println(e.getMessage());
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete tree.
	 *
	 * @param uid the uid
	 * @param tree the tree
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTree(@HeaderParam("uid") int uid,Tree tree) {
		try {
			lm.lock(ISPRODUCER, tree.getId(), uid);
			ptm.deleteTree(tree.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					tree.getId()));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the subnodes.
	 *
	 * @param treeId the tree id
	 * @return the subnodes
	 */
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

	/**
	 * Gets the all users with tree right.
	 *
	 * @param treeId the tree id
	 * @return the all users with tree right
	 */
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

	/**
	 * Sets the user right.
	 *
	 * @param treeId the tree id
	 * @param userId the user id
	 * @param user the user
	 * @return the response
	 */
	@POST
	@Path("{ptreeid}/users/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setUserRight(@PathParam("ptreeid") int treeId,
			@PathParam("uid") int userId, @HeaderParam("uid") int uid, User user) {
		try {
			if(!user.isTreeaccess()){
				lm.lock(ISPRODUCER, treeId, uid);
			}
			user.setId(userId);
			UserManager um = new UserManager();
			um.setConsumerUserRight(treeId, user);
			return StatusHelper.getStatusOk(null);
		}  catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		}catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
