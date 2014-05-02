package cdar.jersey.integrationtest;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import cdar.pl.controller.UserController;
import cdar.pl.controller.consumer.ProjectTreeController;

public class TestProjectTreeController extends JerseyTest  {
	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class,ProjectTreeController.class);
	}
}
