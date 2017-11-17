package de.ogli.parts.dbaccess;

import org.hibernate.Session;

public interface ComponentGraphLoader {
	String shortName();
	ComponentGraph loadComponentGraph(Session session, long componentId);
}
