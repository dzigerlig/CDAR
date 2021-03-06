package ch.cdar.pl.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ExtendedUriInfo;

import ch.cdar.bll.entity.User;
import ch.cdar.bll.manager.UserManager;

/**
 * The Class SecurityFilter.
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {
	/* (non-Javadoc)
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		UserManager um = new UserManager();
		final ExtendedUriInfo extendendUriInfo = (ExtendedUriInfo) requestContext
				.getUriInfo();

		if (!extendendUriInfo.getPath().contains("user") && !extendendUriInfo.getPath().contains("descriptions")) {
			int uid = 0;
			String accesstoken = null;
			
			try {
				if (extendendUriInfo.getPath().contains("filexml")) {
					Map<String, List<String>> queryParameters = extendendUriInfo.getQueryParameters();
					uid = Integer.parseInt(queryParameters.get("uid").get(0));
					accesstoken = queryParameters.get("accesstoken").get(0);
				} else {
					uid = Integer.parseInt(requestContext.getHeaderString("uid"));
					accesstoken = requestContext.getHeaderString("accesstoken");
				}
			
				User user = um.getUser(uid);

				if (!user.getAccesstoken().equals(accesstoken)) {
					abortRequest(requestContext);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				abortRequest(requestContext);
			}
		}
	}

	/**
	 * Abort request.
	 *
	 * @param requestContext the request context
	 */
	private void abortRequest(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, "CDAR")
				.entity("Login required.").build());
	}
}
