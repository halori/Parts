package de.ogli.parts.dbaccess;

import java.util.HashSet;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;

public class ComponentGraphLoaderBySqlRecursion implements ComponentGraphLoader {

	public String shortName() {
		return "SQL-Recursion";
	}

	public ComponentGraph loadComponentGraph(Session session, long componentId) {
		HashSet<Component> components = new HashSet<Component>();
		SQLQuery queryComponents = session.createSQLQuery(
				"WITH RECURSIVE parts(id) AS (" + 
				"    SELECT id FROM component where id = "+componentId+ 
				"  UNION" + 
				"    SELECT t.child_id" + 
				"    FROM transformation t, parts p WHERE p.id = t.parent_id" + 
				"  )" + 
				"SELECT * FROM component c WHERE EXISTS (SELECT * FROM parts p WHERE p.id = c.id) ");
		
		queryComponents.addEntity(Component.class);
		@SuppressWarnings("unchecked")
		List<Component> allComponents = (List<Component>) queryComponents.list();
        components.addAll(allComponents);
        
        
        HashSet<Transformation> transformations = new HashSet<Transformation>();
        SQLQuery queryTransformations = session.createSQLQuery(
				"WITH RECURSIVE parts(id) AS (" + 
				"    SELECT id FROM component where id = "+componentId+ 
				"  UNION" + 
				"    SELECT t.child_id" + 
				"    FROM transformation t, parts p WHERE p.id = t.parent_id" + 
				"  )" + 
				"SELECT * FROM transformation t WHERE EXISTS (SELECT * FROM parts p WHERE p.id = t.parent_id) ");
		
		queryTransformations.addEntity(Transformation.class);
		@SuppressWarnings("unchecked")
		List<Transformation>  allTransformations = (List<Transformation>) queryTransformations.list();
        transformations.addAll(allTransformations);

		return new ComponentGraph(components, transformations);
	}
}
