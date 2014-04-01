package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cdar.dal.persistence.HibernateUtil;
import cdar.dal.persistence.JDBCUtil;
import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class KnowledgeProducerDaoController {

	private UserDaoController udc = new UserDaoController();

	public List<KnowledgeTreeDao> getKnowledgeTrees() {
		try {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			List<?> users = session.createQuery("FROM KnowledgeTreeDao").list();
			List<KnowledgeTreeDao> knowledgeTrees = new ArrayList<KnowledgeTreeDao>();
			for (Iterator<?> iter = users.iterator(); iter.hasNext();) {
				knowledgeTrees.add((KnowledgeTreeDao) iter.next());
			}
			tx.commit();
			return knowledgeTrees;
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

	public void addKnowledgeTreeByUsername(String username, String treeName) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		KnowledgeTreeDao tree1 = new KnowledgeTreeDao(treeName);
		UserDao user = udc.getUserByName(username);
		user.getKnowledgeTrees().add(tree1);
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
	}

	public void addKnowledgeTreeByUid(int uid, String treeName) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		UserDao user = udc.getUserById(uid);
		user.getKnowledgeTrees().add(new KnowledgeTreeDao(treeName));
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
	}

	public KnowledgeTreeDao getKnowledgeTreeById(int treeId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findTreeById");
		query.setInteger("id", treeId);
		List<?> list = query.list();
		KnowledgeTreeDao ktd = null;
		if (!list.isEmpty()) {
			ktd = (KnowledgeTreeDao) list.get(0);
		}
		tx.commit();
		System.out.println(ktd.getId());
		return ktd;
	}

	public KnowledgeTemplateDao getKnowledgeTemplateById(int templateId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findKnowledgeTemplateById");
		query.setInteger("id", templateId);
		List<?> list = query.list();
		KnowledgeTemplateDao kt = null;
		if (!list.isEmpty()) {
			kt = (KnowledgeTemplateDao) list.get(0);
		}
		tx.commit();
		return kt;
	}

	public KnowledgeNodeDao getKnowledgeNodeById(int nodeId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findKnowledgeNodeById");
		query.setInteger("id", nodeId);
		List<?> list = query.list();
		KnowledgeNodeDao kn = null;
		if (!list.isEmpty()) {
			kn = (KnowledgeNodeDao) list.get(0);
		}
		tx.commit();
		return kn;
	}

	public void updateTree(KnowledgeTreeDao tree) {
		try {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(tree);
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

	public int removeKnowledgeTreeById(int uid, int ktreeid) {
		try {
			UserDao user = udc.getUserById(uid);
			KnowledgeTreeDao tree = getKnowledgeTreeById(ktreeid);
			user.deleteKnowledgeTree(tree);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(user);
			tx.commit();

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("delete KnowledgeTreeDao where id = "
					+ ktreeid);
			q.executeUpdate();
			tx.commit();
			return 0;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			return -1;
		}
	}

	public void addKnowledgeTemplate(int treeId, String templateTitle) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeId);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeTemplateDao newTemplate = new KnowledgeTemplateDao(
				templateTitle);
		newTemplate.setKnowledgeTree(tree);
		tree.getKnowledgeTemplates().add(newTemplate);
		session.update(tree);
		tx.commit();
	}

	public int removeKnowledgeTemplate(int ktreeid, int templateId) {
		try {
			KnowledgeTreeDao tree = getKnowledgeTreeById(ktreeid);
			KnowledgeTemplateDao template = getKnowledgeTemplateById(templateId);
			tree.deleteKnowledgeTemplate(template);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(tree);
			tx.commit();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session
					.createQuery("delete KnowledgeTemplateDao where id = "
							+ templateId);
			q.executeUpdate();
			tx.commit();

			return 0;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			return -1;
		}
	}

	public KnowledgeNodeDao addKnowledgeNode(int treeId, String title) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeId);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeNodeDao newNode = new KnowledgeNodeDao(title);
		newNode.setKnowledgeTree(tree);

		tree.getKnowledgeNodes().add(newNode);
		session.update(tree);
		tx.commit();
		return newNode;
	}

	public int removeKnowledgeNode(int ktreeid, int nodeId) {
		try {
			KnowledgeTreeDao tree = getKnowledgeTreeById(ktreeid);
			KnowledgeNodeDao node = getKnowledgeNodeById(nodeId);
			tree.deleteKnowledgeNode(node);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(tree);
			tx.commit();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("delete KnowledgeNodeDao where id = "
					+ nodeId);
			q.executeUpdate();
			tx.commit();

			return 0;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			return -1;
		}
	}

	public int removeKnowledgeNodeLink(int linkid) {
		try {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			tx.commit();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session
					.createQuery("delete KnowledgeNodeLinkDao where id = "
							+ linkid);
			q.executeUpdate();
			tx.commit();
			return 0;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			return -1;
		}
	}

	public void addKnowledgeSubNode(int nodeId, String subNodeTitle) {
		KnowledgeNodeDao node = getKnowledgeNodeById(nodeId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeSubNodeDao newSubNode = new KnowledgeSubNodeDao(subNodeTitle);
		newSubNode.setKnowledgeNode(node);
		node.getKnowledgeSubNodes().add(newSubNode);
		session.update(node);
		tx.commit();
	}

	public KnowledgeNodeLinkDao getKnowledgeNodeLink(int linkid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findKnowledgeNodeLinkById");
		query.setInteger("id", linkid);
		List<?> list = query.list();
		KnowledgeNodeLinkDao knl = null;
		if (!list.isEmpty()) {
			knl = (KnowledgeNodeLinkDao) list.get(0);
		}
		tx.commit();
		return knl;
	}

	public KnowledgeNodeLinkDao addKnowledgeNodeLink(int sourceid,
			int targetid, int treeid) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeid);
		KnowledgeNodeDao sourcenode = getKnowledgeNodeById(sourceid);
		KnowledgeNodeDao targetnode = getKnowledgeNodeById(targetid);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeNodeLinkDao link = new KnowledgeNodeLinkDao();
		link.setKnowledgeSourceNode(sourcenode);
		link.setKnowledgeTargetNode(targetnode);
		link.setKnowledgeTree(tree);
		session.save(link);
		tx.commit();
		return link;
	}

	public DictionaryDao getDictionaryById(int dictionaryid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findDictionaryById");
		query.setInteger("id", dictionaryid);
		List<?> list = query.list();
		DictionaryDao kn = null;
		if (!list.isEmpty()) {
			kn = (DictionaryDao) list.get(0);
		}
		tx.commit();
		return kn;
	}

	public DictionaryDao addDictionary(int treeId, int parentid,
			String dictionaryTitle) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeId);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		DictionaryDao newDictionary = new DictionaryDao(parentid,
				dictionaryTitle);
		newDictionary.setKnowledgeTree(tree);

		tree.getDictionaries().add(newDictionary);
		session.update(tree);
		tx.commit();
		return newDictionary;
	}
	
	public DictionaryDao updateDictionary(DictionaryDao dd) {
		try {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(dd);
			tx.commit();
			return dd;
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

	public int removeDictionary(int ktreeid, int dictionaryid) {
		try {
			KnowledgeTreeDao tree = getKnowledgeTreeById(ktreeid);
			DictionaryDao dictionary = getDictionaryById(dictionaryid);
			tree.deleteDictionary(dictionary);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(tree);
			tx.commit();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("delete Dictionary where id = "
					+ dictionaryid);
			q.executeUpdate();
			tx.commit();

			return 0;
		} catch (RuntimeException e) {
			try {
				Session session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} catch (HibernateException e1) {
				System.out.println("Error rolling back transaction");
			}
			return -1;
		}
	}

	public DictionaryDao addDictionary(int treeid, String dictionaryTitle) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeid);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		DictionaryDao newDictionary = new DictionaryDao(dictionaryTitle);
		newDictionary.setKnowledgeTree(tree);

		tree.getDictionaries().add(newDictionary);
		session.update(tree);
		tx.commit();
		return newDictionary;
	}

	public DictionaryDao addDictionary(int treeid, KnowledgeNodeDao myNode,
			String dictionaryTitle) {
		DictionaryDao dic = new DictionaryDao(getKnowledgeTreeById(treeid),
				getKnowledgeNodeById(myNode.getId()), dictionaryTitle);
		KnowledgeNodeDao node = getKnowledgeNodeById(myNode.getId());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		node.setDictionary(dic);
		session.saveOrUpdate(node);
		tx.commit();
		return dic;
	}

	public DictionaryDao addDictionary(int treeid, String title, int nodeid) {
		KnowledgeTreeDao tree = getKnowledgeTreeById(treeid);
		KnowledgeNodeDao node = getKnowledgeNodeById(nodeid);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		DictionaryDao newDictionary = new DictionaryDao(tree, node, title);

		node.setDictionary(newDictionary);
		session.saveOrUpdate(node);

		tx.commit();
		return newDictionary;
	}
	
	public KnowledgeNodeDao updateNode(KnowledgeNodeDao node) {
		//JDBC WORKAROUND
//		try {
//			Session session = HibernateUtil.getSessionFactory()
//					.getCurrentSession();
//			Transaction tx = session.beginTransaction();
//			session.update(node);
//			tx.commit();
//			return node;
//		} catch (RuntimeException e) {
//			try {
//				Session session = HibernateUtil.getSessionFactory()
//						.getCurrentSession();
//				if (session.getTransaction().isActive())
//					session.getTransaction().rollback();
//			} catch (HibernateException e1) {
//				System.out.println("Error rolling back transaction");
//			}
//			throw e;
//		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE KNOWLEDGENODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG = ? WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new Date(0));
			preparedStatement.setString(2, node.getTitle());
			preparedStatement.setInt(3, node.getDynamicTreeFlag());
			preparedStatement.setInt(4, node.getId());

			preparedStatement.executeUpdate();


		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, null);
		}
		return node;
	}
	
	public void moveKnowledgeNode(int nodeid, int newdictionaryid) {
		//JDBC WORKAROUND
		//update knowledgenodemapping knm SET did = 4 where knm.knid = 4;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement("INSERT INTO KNOWLEDGENODEMAPPING (DID, KNID) VALUES (?, ?)ON DUPLICATE KEY UPDATE DID = ?",
					//"UPDATE KNOWLEDGENODEMAPPING KNM SET DID = ? WHERE KNM.KNID = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, newdictionaryid);
			preparedStatement.setInt(2, nodeid);
			preparedStatement.setInt(3, newdictionaryid);


			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
	}
	
	private void closeConnections(Connection connection,
			PreparedStatement preparedStatement, Statement statement, ResultSet generatedKeys) {
		try {
			if (generatedKeys != null)
				generatedKeys.close();
			if (preparedStatement != null)
				preparedStatement.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
