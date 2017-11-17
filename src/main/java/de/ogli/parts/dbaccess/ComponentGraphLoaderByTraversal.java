package de.ogli.parts.dbaccess;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;

public class ComponentGraphLoaderByTraversal implements ComponentGraphLoader
{

	public String shortName() {
		return "Traversal";
	}
	public ComponentGraph loadComponentGraph(Session session, long componentId) {
		HashSet<Component> components = new HashSet<Component>();
		Component c = (Component) session.load(Component.class, componentId);
        components.add(c);
        Query querySubComponents = session.createQuery("from Component p where exists (from SubPartRelation s where s.partComponentId = p.id and s.mainComponentId = :cid)");
        querySubComponents.setParameter("cid", c.getId());
        @SuppressWarnings("unchecked")
		List<Component> subComponents = (List<Component>) querySubComponents.list();
        components.addAll(subComponents);
        HashSet<Transformation> transformations = new HashSet<Transformation>();
        Query queryDirectTransfomations = session.createQuery("from Transformation t where t.parentComponentId = :cid)");
        queryDirectTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  directTransformations = (List<Transformation>) queryDirectTransfomations.list();
        //System.out.println("Direct tranforms for "+c.getId()+": "+directTransformations.size());
		transformations.addAll(directTransformations);
		Query querySubTransfomations = session.createQuery("from Transformation t where exists (from SubPartRelation s where s.partComponentId = t.parentComponentId and s.mainComponentId = :cid)");
		querySubTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  subTransformations = (List<Transformation>) querySubTransfomations.list();
		System.out.println("Indirect tranforms for "+c.getId()+": "+subTransformations.size());
		transformations.addAll(subTransformations);
		//System.out.println("All transformations for  "+c.getId()+": "+transformations.size());
	    return new ComponentGraph(components, transformations);
	}
}
