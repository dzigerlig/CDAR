package cdar.pl.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.CdarDescriptions;
import cdar.pl.helpers.StatusHelper;

@Path("descriptions")
public class DescriptionController {
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
