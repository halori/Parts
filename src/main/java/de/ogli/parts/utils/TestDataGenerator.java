package de.ogli.parts.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.SubPartRelation;
import de.ogli.parts.entities.Transformation;

public class TestDataGenerator {

	private int componentCount = 0;
	
	private Session session;

	private int BatchSize;

	private ArrayList<Long> savedComponentIds = new ArrayList<Long>();

	private Random rnd = new Random(24543);

	public TestDataGenerator(Session session, int numberOfComponents) {
		this.session = session;
		this.BatchSize = numberOfComponents;
	}

	public void createBatch(boolean fillReachabilityTable) {
		session.beginTransaction();
		HashMap<Long, HashSet<Long>> successorMapForBatch = fillReachabilityTable ? new HashMap<Long, HashSet<Long>>() : null;
		for (int i = 0; i < BatchSize; i++) {
			createComponent(i,successorMapForBatch);
			if (i % 200 == 0) {
				session.getTransaction().commit();
				session.beginTransaction();
				session.clear();
			}
		}
		session.getTransaction().commit();
		savedComponentIds.clear();
	}

	private void createComponent(int i, HashMap<Long, HashSet<Long>> successorMapForBatch) {
		componentCount++;
		boolean isBaseComponent = (i < BatchSize / 3);
		if (isBaseComponent) {
			Component c = new Component("BaseComponent" + componentCount);
			session.save(c);
			savedComponentIds.add(c.getId());
		} else {
			Component c = new Component("Composite" + componentCount);
			session.save(c);
			savedComponentIds.add(c.getId());
			long parentId = c.getId();
			int numberOfSubComponents = rnd.nextInt(10) + 1;
			HashSet<Long> usedSubComponentIds = new HashSet<Long>();
			for (int j = 0; j < numberOfSubComponents; j++) {
				long childId = savedComponentIds.get(rnd.nextInt((3 * savedComponentIds.size()) / 4));

				if (!usedSubComponentIds.contains(childId)) {
					Transformation t = new Transformation(parentId, childId,
							"Tranform" + parentId + "for" + childId);
					session.save(t);
					usedSubComponentIds.add(childId);
					if(successorMapForBatch != null) {
						Relations.addToRelation(successorMapForBatch, parentId, childId);
						HashSet<Long> successorIds = successorMapForBatch.get(parentId);
						HashSet<Long> childSuccessorIds = successorMapForBatch.get(childId);
						if (childSuccessorIds != null) {
							successorIds.addAll(childSuccessorIds);
						}
						saveSubpartRelations(parentId, successorIds);
					}
				}
			}
		}
	}

	private void saveSubpartRelations(long parentId, HashSet<Long> partIds) {
	
		System.out.println(""+partIds.size()+",");
		
		for (long partId : partIds) {
			SubPartRelation edge = new SubPartRelation(parentId, partId);
			session.save(edge);
		}
	}
}