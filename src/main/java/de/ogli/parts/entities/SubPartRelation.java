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
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PARENT_ID")
	@Index(name = "PARENT_IDX")
	private Long mainComponentId;

	@Column(name = "PART_ID")
	@Index(name = "PART_IDX")
	private Long partComponentId;

	public SubPartRelation(Long mainComponentId, Long partComponentId) {
		this.mainComponentId = mainComponentId;
		this.partComponentId = partComponentId;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", MainComponentId=" + mainComponentId + ", partComponentId=" + partComponentId + "]";
	}

	public SubPartRelation() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMainComponentId() {
		return mainComponentId;
	}

	public void setMainComponentId(Long mainComponentId) {
		this.mainComponentId = mainComponentId;
	}

	public Long getPartComponentId() {
		return partComponentId;
	}

	public void setPartComponentId(Long partComponentId) {
		this.partComponentId = partComponentId;
	}
}
