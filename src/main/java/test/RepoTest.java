package test;

import cdar.bll.user.UserManager;

public class RepoTest {

	public static void main(String[] args) {
		try {
			new UserManager().getUser("Bill Gates");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
