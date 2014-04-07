package cdar.dal.persistence.hibernate.knowledgeconsumer;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cdar.dal.persistence.HibernateUtil;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;
import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class KnowledgeConsumerDaoController {

	private UserDaoController udc = new UserDaoController();

	public void addKnowledgeProjectTreeByUsername(String testUsername,
			String treeName) {
		addKnowledgeProjectTreeByUid(udc.getUserByName(testUsername).getId(), treeName);
	}

	public void addKnowledgeProjectTreeByUid(int uid, String treeName) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		UserDao user = udc.getUserById(uid);
		user.getKnowledgeProjectTrees().add(
				new KnowledgeProjectTreeDao(treeName));
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
	}

	public KnowledgeProjectTreeDao getKnowledgeProjectTreeById(int treeId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findProjectTreeById");
		query.setInteger("id", treeId);
		List<?> list = query.list();
		KnowledgeProjectTreeDao ktd = null;
		if (!list.isEmpty()) {
			ktd = (KnowledgeProjectTreeDao) list.get(0);
		}
		tx.commit();
		return ktd;
	}

	public int removeKnowledgeProjectTreeById(int uid, int treeId) {
		try {
			UserDao user = udc.getUserById(uid);
			KnowledgeProjectTreeDao tree = getKnowledgeProjectTreeById(treeId);
			user.deleteKnowledgeProjectTree(tree);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(user);
			tx.commit();

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("delete KnowledgeProjectTreeDao where id = "
					+ treeId);
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

	public void addKnowledgeProjectNode(int projectTreeId, String title) {
		addKnowledgeProjectNode(projectTreeId, title, null);
	}

	public int removeKnowledgeProjectNode(int projectTreeId, int nodeId) {
		try {
			KnowledgeProjectTreeDao tree = getKnowledgeProjectTreeById(projectTreeId);
			KnowledgeProjectNodeDao node = getKnowledgeProjectNodeById(nodeId);
			tree.deleteKnowledgeProjectNode(node);
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.update(tree);
			tx.commit();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query q = session.createQuery("delete KnowledgeProjectNodeDao where id = "
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
	
	public KnowledgeProjectNodeDao getKnowledgeProjectNodeById(int nodeId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.getNamedQuery("findKnowledgeProjectNodeById");
		query.setInteger("id", nodeId);
		List<?> list = query.list();
		KnowledgeProjectNodeDao kpn = null;
		if (!list.isEmpty()) {
			kpn = (KnowledgeProjectNodeDao) list.get(0);
		}
		tx.commit();
		return kpn;
	}

	public void addKnowledgeProjectSubNode(int nodeId, String subNodeTitle) {
		KnowledgeProjectNodeDao node = getKnowledgeProjectNodeById(nodeId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeProjectSubNodeDao newSubNode = new KnowledgeProjectSubNodeDao(subNodeTitle);
		newSubNode.setKnowledgeProjectNode(node);
		node.getKnowledgeProjectSubNodes().add(newSubNode);
		session.update(node);
		tx.commit();
	}

	public void addKnowledgeTreeToProjectTree(int ktreeid, int ptreeid) {
		KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
		KnowledgeTreeDao knowledgeTree = kpdc.getKnowledgeTreeById(ktreeid);
		
		for (KnowledgeNodeDao knn : knowledgeTree.getKnowledgeNodes()) {
			addKnowledgeProjectNode(ptreeid, knn.getTitle(), knn.getWikititle());
		}
	}

	private void addKnowledgeProjectNode(int ptreeid, String title,
			String wikititle) {
		KnowledgeProjectTreeDao tree = getKnowledgeProjectTreeById(ptreeid);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		KnowledgeProjectNodeDao newProjectNode = new KnowledgeProjectNodeDao(title, wikititle);
		newProjectNode.setKnowledgeProjectTree(tree);
		tree.getKnowledgeProjectNodes().add(newProjectNode);
		session.update(tree);
		tx.commit();
	}

}
