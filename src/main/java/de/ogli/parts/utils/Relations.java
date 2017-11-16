package de.ogli.parts.utils;

import java.util.HashMap;
import java.util.HashSet;

public class Relations {

	/**
	 * Destructively add new pair to existing relation
	 */
	public static<T> void addToRelation(HashMap<T, HashSet<T>> relation, T a, T b) {
	  HashSet<T> successorsA = relation.get(a);
	  if (successorsA == null) {
		  successorsA = new HashSet<T>();
		  relation.put(a, successorsA);
	  }
	  successorsA.add(b);
	}
}