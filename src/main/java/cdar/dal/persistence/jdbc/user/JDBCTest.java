package cdar.dal.persistence.jdbc.user;

import java.sql.SQLException;

public class JDBCTest {

	public static void main(String[] args) throws SQLException {
		UserDaoController udc = new UserDaoController();
		UserDao user = udc.createUser(new UserDao("ok", "nice"));
		System.out.println("User id: " + user.getId());
		UserDao user2 = udc.createUser(new UserDao("ok", "nice"));
		System.out.println("User id: " + user2.getId());
	}
}
