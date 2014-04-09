package cdar.dal.persistence.hibernate.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cdar.bll.model.user.User;
import cdar.dal.persistence.HibernateUtil;

public class UserDaoController {

	public UserDaoController() {
		
	}
	
	public UserDao getUserById(int id) {
		Session session = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		Transaction tx = session.beginTransaction();
		UserDao user = (UserDao) session.get(UserDao.class, id);
		tx.commit();
		return user;
	}
	
	public UserDao getUserByName(String username) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findUserByName");
		query.setString("username", username);
		List<?> list = query.list();
		UserDao user = null;
		if (!list.isEmpty()) {
			user = (UserDao) list.get(0);
		}
		tx.commit();
		return user;
	}
	
	public List<UserDao> getUsers() {
		try {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			List<?> users = session.createQuery("FROM UserDao").list();
			List<UserDao> userList = new ArrayList<UserDao>();
			for (Iterator<?> iter = users.iterator(); iter.hasNext();) {
				userList.add((UserDao) iter.next());
			}
			tx.commit();
			return userList;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			throw e;
		}
	}

	public void updateUserPasswordById(int id, String newPassword) {
			UserDao user = getUserById(id);
			user.setPassword(newPassword);
			updateUser(user);
	}
	
	public void updateUser(UserDao user) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			throw e;
		}
	}

	public void deleteUserById(int id) {
		try {
			Transaction tx = null;
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			UserDao user = (UserDao) session.get(UserDao.class, id);
			session.delete(user);
			
			tx.commit();
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			throw e;
		}
	}

	public UserDao createUser(String username, String password) {
		try {
			if (getUserByName(username)==null) {
				UserDao user = new UserDao();
				user.setUsername(username);
				user.setPassword(password);

				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				Transaction tx = session.beginTransaction();
				session.save(user);
				tx.commit();
				return user;
			} else {
				throw new Exception("User already exists");
			}
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public User createUser(User user) {
		//return new User(createUser(user.getUsername(), user.getPassword()));
		return null;
	}
	
	public void setPassword(int uid, String password) {
		UserDao user = getUserById(uid);
		user.setPassword(password);
		Session session = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.update(user);
		tx.commit();
	}
	
	public void setAccessToken(int uid, String accessToken) {
		UserDao user = getUserById(uid);
		user.setAccesstoken(accessToken);
		Session session = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.update(user);
		tx.commit();
	}
	
	public void setAccessToken(int uid) {
		String sha1;
		try {
			sha1 = sha1(String.format("%s%s",getUserById(uid).getPassword(),new Date().toString()));
			setAccessToken(uid, sha1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	//SOURCE: http://www.sha1-online.com/sha1-java/
	static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }

	public UserDao getUserByAccessToken(String accessToken) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findUserByAccessToken");
		query.setString("accesstoken", accessToken);
		List<?> list = query.list();
		UserDao user = null;
		if (!list.isEmpty()) {
			user = (UserDao) list.get(0);
		}
		tx.commit();
		return user;
	}

	public UserDao loginUser(String username, String password) {
		UserDao user = getUserByName(username);
		if (user!=null && user.getPassword().equals(password)) {
			setAccessToken(user.getId());
			return getUserByName(username);
		}
		return null;
	}
}
