package de.ogli.parts.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;
import de.ogli.parts.entities.TransformationId;

public class TestDataGenerator {

	private Session session;

	private int BatchSize;

	private ArrayList<Long> savedComponentIds = new ArrayList<Long>();

	private Random rnd = new Random(24543);

	public TestDataGenerator(Session session, int numberOfComponents) {
		this.session = session;
		this.BatchSize = numberOfComponents;
	}

	public void createBatch() {
		session.beginTransaction();
		for (int i = 0; i < BatchSize; i++) {
			createComponent(i);
			if (i % 200 == 0) {
				session.getTransaction().commit();
				session.beginTransaction();
				session.clear();
			}
		}
		session.getTransaction().commit();
		savedComponentIds.clear();
	}

	private void createComponent(int i) {
		boolean isBaseComponent = (i < BatchSize / 2);
		if (isBaseComponent) {
			Component c = new Component("BaseComponent" + i);
			session.save(c);
			savedComponentIds.add(c.getId());
		} else {
			Component c = new Component("Composite" + i);
			session.save(c);
			savedComponentIds.add(c.getId());
			long parentId = c.getId();
			int numberOfSubComponents = rnd.nextInt(9) + 1;
			HashSet<Long> usedSubComponentIds = new HashSet<Long>();
			for (int j = 0; j < numberOfSubComponents; j++) {
				long childId = savedComponentIds.get(rnd.nextInt(savedComponentIds.size() / 2));

				if (!usedSubComponentIds.contains(childId)) {
					Transformation t = new Transformation(new TransformationId(parentId, childId),
							"Tranform" + j + "for" + i);
					session.save(t);
					usedSubComponentIds.add(childId);
				}
			}
		}
	}
}
