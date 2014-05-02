package cdar.pl.controller;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StatusHelper<T> {
	public static <T> Response getResponseOk(T t) {
		return Response.ok(t, MediaType.APPLICATION_JSON).build();
	}
	
	public static Response getStatusBadRequest() {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	public static <T> Response getStatusCreated(T t) {
		return Response.status(Response.Status.CREATED).entity(t).build();
	}
}
