package cdar.pl.helpers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StatusHelper<T> {
	public static <T> Response getStatusOk(T t) {
		return Response.ok(t, MediaType.APPLICATION_JSON).build();
	}
	
	public static Response getStatusBadRequest() {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	public static <T> Response getStatusCreated(T t) {
		return Response.status(Response.Status.CREATED).entity(t).build();
	}
	
	public static Response getStatusUnauthorized() {
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	public static Response getStatusConflict() {
		return Response.status(Response.Status.CONFLICT).build();
	}
	
	public static <T> Response getStatusConflict(T t) {
		return Response.status(Response.Status.CONFLICT).entity(t).build();
	}
}
