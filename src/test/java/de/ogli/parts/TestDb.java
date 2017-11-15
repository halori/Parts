package de.ogli.parts;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.ogli.parts.utils.TestDataGenerator;

public class TestDb extends TestCase {

	long N = 50000000;
	
	public void testDb() {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		int batchSize = 5000;
		
		for(int i = 0; i < N; i += batchSize) {
			System.out.println("created "+i+" from "+N+" components");
	    	TestDataGenerator tdg = new TestDataGenerator(session, batchSize);
	    	tdg.createBatch();
	    }
		session.close();
	}
}
