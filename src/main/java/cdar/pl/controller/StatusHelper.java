package cdar.pl.controller;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Class StatusHelper.
 *
 * @param <T> the generic type
 */
public class StatusHelper<T> {
	
	/**
	 * Gets the cache control.
	 *
	 * @return the cache control
	 */
	private static CacheControl getCacheControl() {
		CacheControl cc = new CacheControl();
		cc.setNoCache(true);
		cc.setNoStore(true);
		cc.setMustRevalidate(true);
		return cc;
	}
	
	/**
	 * Gets the status ok.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the status ok
	 */
	public static <T> Response getStatusOk(T t) {
		return Response.ok(t, MediaType.APPLICATION_JSON).cacheControl(getCacheControl()).build();
	}
	
	/**
	 * Gets the status bad request.
	 *
	 * @return the status bad request
	 */
	public static Response getStatusBadRequest() {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	/**
	 * Gets the status created.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the status created
	 */
	public static <T> Response getStatusCreated(T t) {
		return Response.status(Response.Status.CREATED).cacheControl(getCacheControl()).entity(t).build();
	}
	
	/**
	 * Gets the status unauthorized.
	 *
	 * @return the status unauthorized
	 */
	public static Response getStatusUnauthorized() {
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	/**
	 * Gets the status conflict.
	 *
	 * @return the status conflict
	 */
	public static Response getStatusConflict() {
		return Response.status(Response.Status.CONFLICT).build();
	}
	
	/**
	 * Gets the status conflict.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the status conflict
	 */
	public static <T> Response getStatusConflict(T t) {
		return Response.status(Response.Status.CONFLICT).entity(t).build();
	}
}
