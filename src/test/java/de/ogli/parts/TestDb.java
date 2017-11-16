package de.ogli.parts;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
		
		ArrayList<Long> allComponentIds = new ArrayList<Long>();
		
		for(int i = 0; i < N; i += batchSize) {
			System.out.println("created "+i+" from "+allComponentIds.size()+" components");
	    	TestDataGenerator tdg = new TestDataGenerator(session, batchSize);
			String nameSuffix = ""+allComponentIds.size();
			HashMap<Long, HashSet<Long>> subPartRelation = tdg.createBatch(nameSuffix );
	        allComponentIds.addAll(subPartRelation.keySet());
		}
		session.close();
	}
}
