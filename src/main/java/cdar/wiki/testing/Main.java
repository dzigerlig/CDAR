package cdar.wiki.testing;

import java.io.IOException;
import java.util.Date;

import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

public class Main {

	public static void main(String[] args) throws IOException, LoginException {
		final String username = "mynewusername";
		final String password = "mynewpassword";
		
		
		Wiki c = new Wiki();
		c.login(username, password);
		//c.login("admin", "password");
		
		
		
		c.edit("MYWIKITITLE222 2", "TEST", "");
		c.getPageText("OK");
	}

}
