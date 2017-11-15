package de.ogli.parts.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Transformation implements Serializable
{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TransformationId id;
	
	 @Column(name = "MAPPING", length = 200, nullable = false)
	    private String mapping;

	@Override
	public String toString() {
		return "[id=" + id + ": "+ mapping + "]";
	}

	public Transformation(TransformationId id, String mapping) {
		this.id = id;
		this.mapping = mapping;
	}

	public Transformation() {
	}

	public TransformationId getId() {
		return id;
	}

	public void setId(TransformationId id) {
		this.id = id;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	} 
}
