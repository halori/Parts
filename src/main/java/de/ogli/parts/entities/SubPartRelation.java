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
@Table(name = "SUBPARTRELATION")
public class SubPartRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 18, scale = 0)
	private Long id;

	@Index(name = "PARENT_IDX")
	@Column(name = "SR_PARENT_ID", nullable = false, precision = 18, scale = 0)
	private Long parentComponentId;

	@Index(name = "PART_IDX")
	@Column(name = "SR_PART_ID", nullable = false, precision = 18, scale = 0)
	private Long partComponentId;

	public SubPartRelation() {
	}

	public SubPartRelation(Long parentComponentId, Long partComponentId) {
		this.parentComponentId = parentComponentId;
		this.partComponentId = partComponentId;
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

	public Long getPartComponentId() {
		return partComponentId;
	}

	public void setChildComponentId(Long partComponentId) {
		this.partComponentId = partComponentId;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", ParentComponentId=" + parentComponentId + ", partComponentId=" + partComponentId + "]";
	}
}
