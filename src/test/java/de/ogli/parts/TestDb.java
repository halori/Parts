package de.ogli.parts;

import junit.framework.TestCase;

import java.awt.font.TransformAttribute;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.ogli.parts.entities.Component;
import de.ogli.parts.entities.Transformation;
import de.ogli.parts.entities.TransformationId;

public class TestDb extends TestCase {

	public void testDb() {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Component c1 = new Component("Test1");
		session.save(c1);

		Component c2 = new Component("Test1");
		session.save(c2);
		
		Transformation t = new Transformation(new TransformationId(c1.getId(), c2.getId()), "Rotate(x,y,z,degree).Move(u,v,w)");

		session.save(t);
		
		session.getTransaction().commit();
		session.close();
	}
}
