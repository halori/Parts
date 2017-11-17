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
	@Column(name = "SR_Main_ID", nullable = false, precision = 18, scale = 0)
	private Long mainComponentId;

	@Index(name = "PART_IDX")
	@Column(name = "SR_PART_ID", nullable = false, precision = 18, scale = 0)
	private Long partComponentId;

	public SubPartRelation() {
	}

	public SubPartRelation(Long mainComponentId, Long partComponentId) {
		this.mainComponentId = mainComponentId;
		this.partComponentId = partComponentId;
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

	@Override
	public String toString() {
		return "[id=" + id + ", MainComponentId=" + mainComponentId + ", partComponentId=" + partComponentId + "]";
	}
}
