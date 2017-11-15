package de.ogli.parts.entities;

import java.io.Serializable;

import javax.persistence.Column;

public class TransformationId implements Serializable {
   
	private static final long serialVersionUID = 1L;

	@Column(name = "COMPONENT_ID", nullable = false, precision = 18, scale = 0)
    private long component;
    
    @Column(name = "SUB_COMPONENT_ID", nullable = false, precision = 18, scale = 0)
    private long subComponent;
    

	public TransformationId(long component, long subComponent) {
		this.component = component;
		this.subComponent = subComponent;
	}
	
	
	public TransformationId() {
	}


	public long getComponent() {
		return component;
	}

	public void setComponent(long component) {
		this.component = component;
	}


	public long getSubComponent() {
		return subComponent;
	}


	public void setSubComponent(long subComponent) {
		this.subComponent = subComponent;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (component ^ (component >>> 32));
		result = prime * result + (int) (subComponent ^ (subComponent >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransformationId other = (TransformationId) obj;
		if (component != other.component)
			return false;
		if (subComponent != other.subComponent)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "[component=" + component + ", subComponent=" + subComponent + "]";
	}
}
