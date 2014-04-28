package test;

import cdar.bll.user.UserModel;

public class RepoTest {

	public static void main(String[] args) {
		try {
			new UserModel().getUser("Bill Gates");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
