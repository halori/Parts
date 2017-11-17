package de.ogli.parts;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.ogli.parts.dbaccess.ComponentGraph;
import de.ogli.parts.dbaccess.ComponentGraphLoader;
import de.ogli.parts.dbaccess.ComponentGraphLoaderBySqlRecursion;
import de.ogli.parts.dbaccess.ComponentGraphLoaderBySubpartRelation;
import de.ogli.parts.dbaccess.ComponentGraphLoaderByTraversal;
import de.ogli.parts.utils.TestDataGenerator;
import junit.framework.TestCase;

public class TestDb extends TestCase {

	static final long N = 50000000;

	static final long testMinSize = 30; // minimum size of tested components
	static final long testMaxSize = 60; // maximum size of tested components
	static final long numberOfTestsPerAdditionalBatch = 100; // number of test runs
	static final long pauseBeforePerformanceTestMillis = 1 * 60 * 1000;
	static final int batchSize = 5000;	static final ComponentGraphLoader loadersToTest[] = { new ComponentGraphLoaderBySubpartRelation(),
			new ComponentGraphLoaderByTraversal(), new ComponentGraphLoaderBySqlRecursion() };

	public void testDb() throws IOException {

		Random rnd = new Random(72256);
		PrintWriter writer = new PrintWriter("performance.txt");
		StringBuilder header = new StringBuilder();
		header.append("dbsize, batchsize, write-batch(ms)");
		for (int i = 0; i < loadersToTest.length; i++) {
			header.append(", ").append(loadersToTest[i].shortName()).append("(ms)");
		}
		writer.write(header.toString());
		writer.write("\n");

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

			System.out.println("sampling " + numberOfTestsPerAdditionalBatch + " from "
					+ allComponentIdsForTestSize.size() + " suitable components.");
			ArrayList<Long> sample = getSample(allComponentIdsForTestSize, numberOfTestsPerAdditionalBatch, rnd);
			try {
				Thread.sleep(pauseBeforePerformanceTestMillis);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			writer.write("" + (i + batchSize) + ", " + batchSize + ", " + millisDuration);
			
			doPerformanceMeasurement(writer, sessionFactory, i, sample);
			writer.write("\n");
			writer.flush();
		}
		writer.close();
	}

	private void doPerformanceMeasurement(PrintWriter writer, SessionFactory sessionFactory, int numberOfComponents,
			ArrayList<Long> sample) {

		System.out.print("Start test. ");
		int loaderCnt = loadersToTest.length;

		ComponentGraph results[][] = new ComponentGraph[loaderCnt][sample.size()];
		long durationMillis[] = new long[loaderCnt];

		for (int runForCachingEffects = 0; runForCachingEffects < 2; runForCachingEffects++) {
			for (int loaderIdx = 0; loaderIdx < loaderCnt; loaderIdx++) {

				long millisAtStart = System.currentTimeMillis();
				Session session = sessionFactory.openSession();
				session.setCacheMode(CacheMode.IGNORE);

				for (int i = 0; i < sample.size(); i++) {
					session.beginTransaction();
					ComponentGraph cg = loadersToTest[loaderIdx].loadComponentGraph(session, sample.get(i));
					results[loaderIdx][i] = cg;
					session.getTransaction().commit();
					session.clear();
				}

				durationMillis[loaderIdx] = System.currentTimeMillis() - millisAtStart;
			}
		}

		StringBuilder durationsText = new StringBuilder();
		for (int loaderIdx = 0; loaderIdx < loaderCnt; loaderIdx++) {
			
			writer.write(", "+durationMillis[loaderIdx]);
			
			if (durationsText.length() > 0) {
				durationsText.append(", ");
			}
			durationsText.append(loadersToTest[loaderIdx].shortName());
			durationsText.append(":");
			durationsText.append(durationMillis[loaderIdx]);
		}

		System.out.println("Duration: " + durationsText.toString() + "ms");
		for (int loaderIdx = 1; loaderIdx < loaderCnt; loaderIdx++) {

			for (int i = 0; i < sample.size(); i++) {
				ComponentGraph gc0 = results[0][i];
				ComponentGraph gci = results[loaderIdx][i];
				// check is not sufficient for equality
				assertEquals(gc0.components.size(), gci.components.size());
				assertEquals(gc0.transformations.size(), gci.transformations.size());
			}
		}
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
