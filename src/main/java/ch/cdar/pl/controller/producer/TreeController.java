package ch.cdar.pl.controller.producer;

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
import ch.cdar.bll.exceptions.LockingException;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.pl.controller.StatusHelper;

/**
 * The Class TreeController.
 */
@Path("ktrees")
public class TreeController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = true;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The ktm. */
	private TreeManager ktm = new TreeManager(UserRole.PRODUCER);

	/**
	 * Gets the knowledge trees.
	 *
	 * @param uid the uid
	 * @return the knowledge trees
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTrees(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ktm.getTrees(uid));
		} catch (Exception e) {
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
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTree(@HeaderParam("uid") int uid,Tree tree) {
		try {
			lm.lock(ISPRODUCER, tree.getId(), uid);
			ktm.deleteTree(tree.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					tree.getId()));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the knowledge tree.
	 *
	 * @param uid the uid
	 * @param tree the tree
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTree(@HeaderParam("uid") int uid, Tree tree) {
		try {
			return StatusHelper.getStatusCreated(ktm.addTree(uid, tree));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the knowledge tree by id.
	 *
	 * @param treeId the tree id
	 * @return the knowledge tree by id
	 */
	@GET
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTreeById(@PathParam("ktreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(ktm.getTree(treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update knowledge tree.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param tree the tree
	 * @return the response
	 */
	@POST
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeTree(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Tree tree) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			tree.setId(treeId);
			return StatusHelper.getStatusOk(ktm.updateTree(tree));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the subnodes by tree.
	 *
	 * @param treeId the tree id
	 * @return the subnodes by tree
	 */
	@GET
	@Path("{ktreeid}/subnodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodesByTree(@PathParam("ktreeid") int treeId) {
		try {
			SubnodeManager sm = new SubnodeManager();
			return StatusHelper.getStatusOk(sm.getSubnodesFromTree(treeId));
		} catch (Exception e) {
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
	@Path("{ktreeid}/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithTreeRight(@PathParam("ktreeid") int treeId) {
		try {
			UserManager um = new UserManager();
			List<User> userList = um.getUsersByTree(true, treeId);
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
	@Path("{ktreeid}/users/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setUserRight(@PathParam("ktreeid") int treeId,
			@PathParam("uid") int userId, User user) {
		try {
			user.setId(userId);
			UserManager um = new UserManager();
			um.setProducerUserRight(treeId, user);
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
}
