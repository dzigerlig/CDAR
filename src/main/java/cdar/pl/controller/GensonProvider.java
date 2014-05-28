package cdar.pl.controller;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.owlike.genson.Genson;

/**
 * The Class GensonProvider.
 */
@Provider
public class GensonProvider implements ContextResolver<Genson> {
   
   /** The genson. */
   private final Genson genson = new Genson.Builder().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).create();

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	@Override
	public Genson getContext(Class<?> type) {
		return genson;
	}
}