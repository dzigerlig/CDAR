package cdar.pl.controller;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cdar.bll.model.user.User;
import cdar.bll.model.user.UserModel;

@Path("users")
public class UserController {
	private UserModel userModel = new UserModel();

	public UserController() {
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return userModel.getUsers();
	}
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		return userModel.loginUser(username, password);
	}
	
	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	public User createUser(User user) {
		return userModel.createUser(user.getUsername(), user.getPassword());
	}
	
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	public User editUser(User user) {
		return userModel.updateUser(user);
	}
}
