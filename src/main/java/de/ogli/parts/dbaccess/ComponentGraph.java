package de.ogli.parts.dbaccess;

import java.util.HashSet;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;
import de.ogli.parts.utils.SetUtils;

/**
 * A component graph consists of a) several components as nodes, and b)
 * transformations as edges. The Graph is acyclic, multiple edges are allowed.
 */
public class ComponentGraph {
	final public HashSet<Component> components;
	final public HashSet<Transformation> transformations;

	public ComponentGraph(HashSet<Component> components, HashSet<Transformation> transformations) {
		this.components = components;
		this.transformations = transformations;
	}

	public boolean equalsByIds(ComponentGraph other) {
		if (this.components.size() != other.components.size()
				|| this.transformations.size() != other.transformations.size())
			return false;
		HashSet<Long> componentIdsA = this.getComponentIds();
		HashSet<Long> componentIdsB = other.getComponentIds();
		boolean eq = SetUtils.equalByElements(componentIdsA, componentIdsB);
		if (!eq)
			return false;
		HashSet<Long> transformationIdsA = this.gettransformationIds();
		HashSet<Long> transformationIdsB = other.gettransformationIds();
		return SetUtils.equalByElements(transformationIdsA, transformationIdsB);
	}

	public HashSet<Long> getComponentIds() {
		HashSet<Long> result = new HashSet<Long>();
		for (Component component : components) {
			result.add(component.getId());
		}
		return result;
	}

	public HashSet<Long> gettransformationIds() {
		HashSet<Long> result = new HashSet<Long>();
		for (Transformation transformation : transformations) {
			result.add(transformation.getId());
		}
		return result;
	}
}
