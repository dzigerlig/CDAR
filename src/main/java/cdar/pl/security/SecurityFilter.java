package cdar.pl.security;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ExtendedUriInfo;

import cdar.bll.user.User;
import cdar.bll.user.UserModel;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		UserModel um = new UserModel();
		final ExtendedUriInfo extendendUriInfo = (ExtendedUriInfo) requestContext
				.getUriInfo();
		
		if (!extendendUriInfo.getPath().contains("user")) {
			try {
				final int uid = Integer.parseInt(extendendUriInfo.getPathParameters().get("uid").get(0));
				final String accesstoken = extendendUriInfo.getPathParameters().get("accesstoken").get(0);
				
				User user = um.getUser(uid);
				
				if (!user.getAccesstoken().equals(accesstoken)) {
					abortRequest(requestContext);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				abortRequest(requestContext);
			}
		}
	}
	
	private void abortRequest(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, "CDAR").entity("Login required.").build());
	}
}
