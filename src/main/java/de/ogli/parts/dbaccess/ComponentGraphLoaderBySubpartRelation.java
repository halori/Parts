package de.ogli.parts.dbaccess;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;

public class ComponentGraphLoaderBySubpartRelation implements ComponentGraphLoader
{

	public String shortName() {
		return "Subpart-Table";
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
        transformations.addAll(directTransformations);
		Query querySubTransfomations = session.createQuery("from Transformation t where exists (from SubPartRelation s where s.partComponentId = t.parentComponentId and s.mainComponentId = :cid)");
		querySubTransfomations.setParameter("cid", c.getId());
		@SuppressWarnings("unchecked")
		List<Transformation>  subTransformations = (List<Transformation>) querySubTransfomations.list();
        transformations.addAll(subTransformations);
		return new ComponentGraph(components, transformations);
	}
}
