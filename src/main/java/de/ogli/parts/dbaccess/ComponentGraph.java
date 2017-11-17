package de.ogli.parts.dbaccess;

import java.util.HashSet;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;

public class ComponentGraph {
	final public HashSet<Component> components;
	final public HashSet<Transformation> transformations;
	
	public ComponentGraph(HashSet<Component> components, HashSet<Transformation> transformations) {
		this.components = components;
		this.transformations = transformations;
	}
}
