package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.user.User;
import cdar.bll.user.UserModel;

@Path("users")
public class UserController {
	private UserModel userModel = new UserModel();

	public UserController() {
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		try {
			return Response
					.ok(userModel.getUsers(), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		try {
			return Response.ok(userModel.loginUser(username, password),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		try {
			return Response
					.ok(userModel.createUser(user.getUsername(),
							user.getPassword()), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception ex) {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}

	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(User user) {
		try {
			return Response.ok(userModel.updateUser(user),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(int userid) {
		try {
			return Response.ok(userModel.deleteUser(userid), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.EXPECTATION_FAILED).build();
		}
	}
}
