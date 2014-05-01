package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;

@Path("users")
public class UserController {
	private UserManager userManager = new UserManager();

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		try {
			User loggedInUser = userManager.loginUser(username, password);
			loggedInUser.setPassword(null);
			return Response.ok(loggedInUser,
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		try {
			user = userManager.createUser(user.getUsername(), user.getPassword());
			user.setPassword(null);
			return Response.status(Response.Status.CREATED).entity(user).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}

	@POST
	@Path("{uid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(User user) {
		try {
			user = userManager.updateUser(user);
			user.setPassword(null);
			return Response.ok(user,
					MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(int userId) {
		try {
			return Response.ok(userManager.deleteUser(userId), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
