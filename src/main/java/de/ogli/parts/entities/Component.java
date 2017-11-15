package de.ogli.parts.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Component implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 18, scale = 0)
    private Long id;
   
    @Column(name = "NAME", length = 30, nullable = false)
    private String name;

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + "]";
	}

	public Component(String name) {
		this.name = name;
	}



	public Component() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
