package de.ogli.parts.dbaccess;

import org.hibernate.Session;

/**
 * Interface for performance measurement of different loading strategies. Only
 * ONE implementation will be used in the application, the interface can then be
 * removed!
 */
public interface ComponentGraphLoader {
	String shortName();

	ComponentGraph loadComponentGraph(Session session, long componentId);
}
