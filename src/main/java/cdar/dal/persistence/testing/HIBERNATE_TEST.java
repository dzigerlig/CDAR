package cdar.dal.persistence.testing;


import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cdar.bll.model.TreeModel;
import cdar.bll.model.user.User;
import cdar.bll.producer.Tree;
import cdar.dal.persistence.HibernateUtil;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTemplateDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;
import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;


public class HIBERNATE_TEST {
	private static UserDaoController udc = new UserDaoController();
	private static KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
	
	private static String testUsername = "UnitTestUser";
	private static String testPassword = "UnitTestPassword";
	
	public static void main(String[] args) {
		//udc.createUser(testUsername, testPassword);
		
		
		udc.deleteUserById(udc.getUserByName(testUsername).getId());
	}

}
