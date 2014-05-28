package ch.cdar.pl.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.CdarDescriptions;

/**
 * The Class DescriptionController.
 */
@Path("descriptions")
public class DescriptionController {
	
	/**
	 * Gets the descriptions.
	 *
	 * @return the descriptions
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDescriptions() {
		try {
			return StatusHelper.getStatusOk(new CdarDescriptions());
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}