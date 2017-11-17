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

	private final int BatchSize;

	private final Random rnd = new Random(24543);

	public TestDataGenerator(int numberOfComponents) {
		this.BatchSize = numberOfComponents;
	}

	public HashMap<Long, HashSet<Long>> createBatch(Session session, String nameSuffix) {
		ArrayList<Long> savedComponentIds = new ArrayList<Long>();

		session.beginTransaction();
		HashMap<Long, HashSet<Long>> subPartRelation = 
				new HashMap<Long, HashSet<Long>>();
		for (int i = 0; i < BatchSize; i++) {
			createComponent(session, nameSuffix, i, subPartRelation, savedComponentIds);
			if (i % 100 == 0) {
				session.getTransaction().commit();
				session.beginTransaction();
				session.clear();
			}
		}
		session.getTransaction().commit();
		return subPartRelation;
	}

	private void createComponent(Session session, String nameSuffix, int i, HashMap<Long, HashSet<Long>> SubPartRelationForBatch,
			ArrayList<Long> savedComponentIds) {
		boolean isBaseComponent = (i < BatchSize / 3);
		if (isBaseComponent) {
			Component c = new Component("BaseComponent" + i + nameSuffix);
			session.save(c);
			savedComponentIds.add(c.getId());
			SubPartRelationForBatch.put(c.getId(), new HashSet<Long>());
		} else {
			Component c = new Component("Composite" + nameSuffix);
			session.save(c);
			savedComponentIds.add(c.getId());
			long parentId = c.getId();
			int numberOfSubComponents = rnd.nextInt(10) + 1;
			HashSet<Long> usedSubComponentIds = new HashSet<Long>();
			for (int j = 0; j < numberOfSubComponents; j++) {
				long childId = savedComponentIds.get(rnd.nextInt((3 * savedComponentIds.size()) / 4));
				Transformation t = new Transformation(parentId, childId, "Tranform" + parentId + "for" + childId);
				session.save(t);
				usedSubComponentIds.add(childId);
				Relations.addToRelation(SubPartRelationForBatch, parentId, childId);
				HashSet<Long> subpartIds = SubPartRelationForBatch.get(parentId);
				HashSet<Long> childSubpartIds = SubPartRelationForBatch.get(childId);
				subpartIds.addAll(childSubpartIds);
			}
			HashSet<Long> successorIds = SubPartRelationForBatch.get(parentId);
			saveSubpartRelations(session,parentId, successorIds);
		}
	}

	private void saveSubpartRelations(Session session, long parentId, HashSet<Long> partIds) {

		//System.out.print(""+partIds.size()+",");

		for (long partId : partIds) {
			SubPartRelation edge = new SubPartRelation(parentId, partId);
			session.save(edge);
		}
	}
}