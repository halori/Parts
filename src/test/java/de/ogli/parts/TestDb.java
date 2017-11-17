package de.ogli.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.ogli.parts.dbaccess.ComponentGraph;
import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;
import de.ogli.parts.utils.TestDataGenerator;
import junit.framework.TestCase;

public class TestDb extends TestCase {

	static final long N = 50000000;

	static final long testMinSize = 30; // minimum size of tested components
	static final long testMaxSize = 60; // maximum size of tested components
	static final long numberOfTestsPerAdditionalBatch = 100; // number of test runs
	static final long pauseBeforePerformanceTestMillis = 1 * 60 * 1000;
    static final int batchSize = 5000;

	public void testDb() {

		Random rnd = new Random(72256);
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		
		ArrayList<Long> allComponentIdsForTestSize = new ArrayList<Long>();

		TestDataGenerator tdg = new TestDataGenerator(batchSize);

		for (int i = 0; i < N; i += batchSize) {

			long millisAtStart = System.currentTimeMillis();
			String nameSuffix = "" + i;
			HashMap<Long, HashSet<Long>> subPartRelation = tdg.createBatch(sessionFactory, nameSuffix);
			for (long id : subPartRelation.keySet()) {
				int size = subPartRelation.get(id).size();
				if (testMinSize <= size && size <= testMaxSize) {
					allComponentIdsForTestSize.add(id);
				}
			}
			long millisDuration = System.currentTimeMillis() - millisAtStart;

			System.out.println("created " + (i + batchSize) + " from " + N + " components."
					+ " Duration for last batch: " + (millisDuration / 1000) + "s");

			System.out.println("sampling "+numberOfTestsPerAdditionalBatch
					+" from "+allComponentIdsForTestSize.size() + " suitable components.");
			ArrayList<Long> sample = getSample(allComponentIdsForTestSize, numberOfTestsPerAdditionalBatch, rnd);
			try {
				Thread.sleep(pauseBeforePerformanceTestMillis);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			doPerformanceMeasurement(sessionFactory, i, sample);

		}

	}

	private void doPerformanceMeasurement(SessionFactory sessionFactory, int numberOfComponents,
			ArrayList<Long> sample) {

		System.out.print("Start test. ");
		long millisAtStart = System.currentTimeMillis();
		Session session = sessionFactory.openSession();
		session.setCacheMode(CacheMode.IGNORE);

		for (int i = 0; i < sample.size(); i++) {
			session.beginTransaction();
			loadAllParts(session, sample.get(i));
			session.getTransaction().commit();
			session.clear();
		}

		long millisDuration = System.currentTimeMillis() - millisAtStart;
		System.out.println("Duration: " + millisDuration + "ms");
	};

	private ComponentGraph loadAllParts(Session session, Long componentId) {
		HashSet<Component> components = new HashSet<Component>();
		Component c = (Component) session.load(Component.class, componentId);
        components.add(c);
        Query querySubComponents = session.createQuery("from Component p where exists (from SubPartRelation s where s.partComponentId = p.id and s.mainComponentId = :cid)");
        querySubComponents.setParameter("cid", c.getId());
        @SuppressWarnings("unchecked")
		List<Component> subComponents = (List<Component>) querySubComponents.list();
        components.addAll(subComponents);
        HashSet<Transformation> transformations = new HashSet<Transformation>();
        Query queryDirectTransfomations = session.createQuery("from Transformation t where t.parentComponentId = :cid)");
        queryDirectTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  directTransformations = (List<Transformation>) queryDirectTransfomations.list();
        transformations.addAll(directTransformations);
		Query querySubTransfomations = session.createQuery("from Transformation t where exists (from SubPartRelation s where s.partComponentId = t.parentComponentId and s.mainComponentId = :cid)");
		querySubTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  subTransformations = (List<Transformation>) querySubTransfomations.list();
        transformations.addAll(subTransformations);
		return new ComponentGraph(components, transformations);
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
