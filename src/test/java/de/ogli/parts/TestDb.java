package de.ogli.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.ogli.parts.entities.Component;
import de.ogli.parts.utils.TestDataGenerator;
import junit.framework.TestCase;

public class TestDb extends TestCase {

	final long N = 50000000;

	final long testMinSize = 30; //minimum size of tested components
	final long testMaxSize = 60; //maximum size of tested components
	final long numberOfTestsPerAdditionalBatch = 100; //number of test runs
	final long pauseBeforePerformanceTestMillis = 7200;
	
	public void testDb() {
		
		Random rnd = new Random(72256);
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		int batchSize = 5000;
		
		ArrayList<Long> allComponentIdsForTestSize = new ArrayList<Long>();
		
		for(int i = 0; i < N; i += batchSize) {
	    	TestDataGenerator tdg = new TestDataGenerator(session, batchSize);
			String nameSuffix = ""+i;
			HashMap<Long, HashSet<Long>> subPartRelation = tdg.createBatch(nameSuffix );
	        for (long id : subPartRelation.keySet()) {
	        	int size = subPartRelation.get(id).size();
	        	if (testMinSize <= size && size <= testMaxSize) {
	        		allComponentIdsForTestSize.addAll(subPartRelation.keySet());
	        	}
	        }
			System.out.println("created "+(i+batchSize)+" from "+N+" components");

	        ArrayList<Long> sample = getSample(allComponentIdsForTestSize, numberOfTestsPerAdditionalBatch, rnd);
		    try {
				Thread.sleep(pauseBeforePerformanceTestMillis);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
	        doPerformanceMeasurement(session, i, sample);
		}
		session.close();
	}

	private void doPerformanceMeasurement(Session session, int numberOfComponents, ArrayList<Long> sample) {
		
		System.out.print("Start test. ");
		long millisAtStart = System.currentTimeMillis();
		
		for (int i = 0; i < sample.size(); i++) {
			session.beginTransaction();
			loadAllParts(session, sample.get(i));
			session.getTransaction().commit();
			session.clear();
		}
		
		long millisDuration = System.currentTimeMillis() - millisAtStart;
		System.out.println("Durationt: "+millisDuration+"ms");
	};

	private void loadAllParts(Session session, Long componentId) {
		session.load(Component.class, componentId);
	}

	private ArrayList<Long> getSample(ArrayList<Long> list, long n, Random rnd) {
		ArrayList<Long> sample = new ArrayList<Long>();
		for (int i = 0; i < n; i++) {
			int idx = rnd.nextInt(list.size());
			sample.add(list.get(idx));
		}
		return sample;
	}
}
