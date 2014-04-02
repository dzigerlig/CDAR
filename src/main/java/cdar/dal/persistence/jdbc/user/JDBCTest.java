package cdar.dal.persistence.jdbc.user;

import java.sql.SQLException;

public class JDBCTest {

	public static void main(String[] args) throws SQLException {
		UserDaoController udc = new UserDaoController();
		System.out.println(udc.getUsers().size());
	}
}
