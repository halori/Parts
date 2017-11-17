package de.ogli.parts.dbaccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;

public class ComponentGraphLoaderBySqlRecursion implements ComponentGraphLoader
{

	public String shortName() {
		return "SQL-Recursion";
	}
	public ComponentGraph loadComponentGraph(Session session, long componentId) {
		HashMap<Long, Component> componentsById = new HashMap<Long, Component>();
		HashSet<Transformation> transformations = new HashSet<Transformation>();
        loadRecursive(session, componentId, componentsById, transformations);
		HashSet<Component> components = new HashSet<Component>();
		components.addAll(componentsById.values());
		return new ComponentGraph(components , transformations);
	}
	
	private void loadRecursive(Session session, 
			long componentId, HashMap<Long, Component> componentsById,
			HashSet<Transformation> transformations) {
		
		if (componentsById.containsKey(componentId))
				return;
		Component c = (Component) session.load(Component.class, componentId);
		componentsById.put(componentId, c);
		Query queryDirectTransfomations = session.createQuery("from Transformation t where t.parentComponentId = :cid)");
        queryDirectTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  directTransformations = (List<Transformation>) queryDirectTransfomations.list();
        for (Transformation t : directTransformations) {
        	transformations.add(t);
        	loadRecursive(session, t.getChildComponentId(), componentsById, transformations);
        	
        }
	}
}
