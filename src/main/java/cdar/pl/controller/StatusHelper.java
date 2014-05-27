package cdar.pl.controller;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StatusHelper<T> {
	
	private static CacheControl getCacheControl() {
		CacheControl cc = new CacheControl();
		cc.setNoCache(true);
		cc.setNoStore(true);
		cc.setMustRevalidate(true);
		return cc;
	}
	
	public static <T> Response getStatusOk(T t) {
		return Response.ok(t, MediaType.APPLICATION_JSON).cacheControl(getCacheControl()).build();
	}
	
	public static Response getStatusBadRequest() {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	public static <T> Response getStatusCreated(T t) {
		return Response.status(Response.Status.CREATED).cacheControl(getCacheControl()).entity(t).build();
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
