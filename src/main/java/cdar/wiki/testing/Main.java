package cdar.wiki.testing;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import cdar.bll.model.wiki.WikiEntry;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeConsumerDaoController;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectNodeDao;


public class Main {

	public static void main(String[] args) throws IOException, LoginException {

	Wiki c = new Wiki();
	c.login("admin", "password");
	c.edit("Test", "edit(Articletitle, Text below your summary, [[Summarytitle]], minor (t/f), bot ,-1<-- for a new summary ,timestamp null means i dont care);", "How to Add a new Summary", false, true,-1,null);
	
	//System.out.println(c.getSectionText("Test", 4));
	//String[] map = c.listPages("", null, 0);
	
//	System.out.println(c.getSectionMap("Test"));
	
	//c.edit("Test", "", "", 0);
	//c.newSection("Test", "second subject", "this is the text of a second subject", false, true);
//	String[] ns=  c.getCategories("Test");
	//String test = c.export("Test");
	//c.edit("Test", "Text", "this is a summary", false, true, -1,null);
	//c.edit("Test", "this is the headertext", "this is a second summary", 0);
	//c.logout();	

		 KnowledgeConsumerDaoController kcdc = new KnowledgeConsumerDaoController();
		 KnowledgeProjectNodeDao kpn = kcdc.getKnowledgeProjectNodeById(1);
		 System.out.println(kpn.getTitle());
	}

	private static void myWikiObjectTest() {
//		WikiEntry we = new WikiEntry("Test");
//		System.out.println("Name: " + we.getName());
//		System.out.println("Text: " + we.getText());
	}

}
