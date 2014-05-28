package ch.cdar.pl.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.User;
import ch.cdar.bll.manager.UserManager;

/**
 * The Class UserController.
 */
@Path("users")
public class UserController {
	
	/** The user manager. */
	private UserManager userManager = new UserManager();
	
	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		try {
			List<User> userList = userManager.getUsers();
			for (User user : userList) {
				user.setPassword("");
				user.setAccesstoken("");
			}
			return Response.ok(userList, MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}
	
	/**
	 * Login.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the response
	 */
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		try {
			User loggedInUser = userManager.loginUser(username, password);
			loggedInUser.setPassword(null);
			return StatusHelper.getStatusOk(loggedInUser);
		} catch (Exception ex) {
			return StatusHelper.getStatusUnauthorized();
		}
	}

	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		try {
			user = userManager.createUser(user, true);
			user.setPassword(null);
			return StatusHelper.getStatusCreated(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			return StatusHelper.getStatusConflict();
		}
	}

	/**
	 * Edits the user.
	 *
	 * @param user the user
	 * @return the response
	 */
	@POST
	@Path("{uid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(User user) {
		try {
			user = userManager.updateUser(user);
			user.setPassword(null);
			return StatusHelper.getStatusOk(user);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	/**
	 * Delete user.
	 *
	 * @param user the user
	 * @return the response
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(User user) {
		try {
			userManager.deleteUser(user.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
