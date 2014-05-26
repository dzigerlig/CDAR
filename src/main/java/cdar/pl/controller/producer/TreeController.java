package cdar.pl.controller.producer;

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

import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.entity.UserRole;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.pl.helpers.StatusHelper;

@Path("ktrees")
public class TreeController {
	private final boolean ISPRODUCER = true;
	private LockingManager lm = new LockingManager();
	private TreeManager ktm = new TreeManager(UserRole.PRODUCER);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTrees(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ktm.getTrees(uid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTree(@HeaderParam("uid") int uid, Tree tree) {
		try {
			return StatusHelper.getStatusCreated(ktm.addTree(uid, tree));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

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
