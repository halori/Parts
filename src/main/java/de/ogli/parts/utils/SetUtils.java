package de.ogli.parts.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SetUtils {

	/**
	 * Destructively add new pair to existing relation
	 */
	public static <T> void addToRelation(HashMap<T, HashSet<T>> relation, T a, T b) {
		HashSet<T> successorsA = relation.get(a);
		if (successorsA == null) {
			successorsA = new HashSet<T>();
			relation.put(a, successorsA);
		}
		successorsA.add(b);
	}

	/**
	 * Checks if to sets contain each other.
	 */
	public static <T> boolean equalByElements(Set<? extends T> A, Set<? extends T> B) {
		return A.size() == B.size() && A.containsAll(B); 
	}
}