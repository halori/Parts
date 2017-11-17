package de.ogli.parts.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@Entity
@Table(name="TRANSFORMATION")
public class Transformation implements Serializable
{
	private static final long serialVersionUID = 1L;

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ID", nullable = false, precision = 18, scale = 0)
	    private Long id;
	   
	    @Index(name="T_PARENT_IDX")
	    @Column(name = "PARENT_ID", nullable = false, precision = 18, scale = 0)
	    private Long parentComponentId;
	   
	    @Index(name="T_CHILD_IDX")
	    @Column(name = "CHILD_ID", nullable = false, precision = 18, scale = 0)
	    private Long childComponentId;
	   
	
	 @Column(name = "MAPPING", length = 200, nullable = false)
	    private String mapping;

		public Transformation() {
	}

		public Transformation(Long parentComponentId, Long childComponentId, String mapping) {
			super();
			this.parentComponentId = parentComponentId;
			this.childComponentId = childComponentId;
			this.mapping = mapping;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getParentComponentId() {
			return parentComponentId;
		}

		public void setParentComponentId(Long parentComponentId) {
			this.parentComponentId = parentComponentId;
		}

		public Long getChildComponentId() {
			return childComponentId;
		}

		public void setChildComponentId(Long childComponentId) {
			this.childComponentId = childComponentId;
		}

		public String getMapping() {
			return mapping;
		}

		public void setMapping(String mapping) {
			this.mapping = mapping;
		}

		@Override
		public String toString() {
			return "[id=" + id + ", ParentComponentId=" + parentComponentId + ", ChildComponentId="
					+ childComponentId + ", mapping=" + mapping + "]";
		}	
}
