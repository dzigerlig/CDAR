package cdar.dal.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
 
    static {
            try {
                Configuration cfg = new Configuration();
                
                String hibernateConfig = System.getProperty("fileName");
                
                if (hibernateConfig!=null) {
                	cfg.configure("hibernateRemote.cfg.xml");  
                } else {
                	cfg.configure("hibernateLocal.cfg.xml");   
                }
                
                StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
                sb.applySettings(cfg.getProperties());
                StandardServiceRegistry standardServiceRegistry = sb.build();                   
                sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);              
            } catch (Throwable th) {
                    System.err.println("Enitial SessionFactory creation failed" + th);
                    throw new ExceptionInInitializerError(th);
            }
    }
     
    public static SessionFactory getSessionFactory() {
            return sessionFactory;
    }
}