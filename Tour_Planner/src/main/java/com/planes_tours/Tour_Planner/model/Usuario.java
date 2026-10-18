package com.planes_tours.Tour_Planner.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@DynamicInsert
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUsuario")
	private Integer idUsuario;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellido")
	private String apellido;

	@Column(name = "email")
	private String email;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "contrasena")
	private String contrasena;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "estado")
	private Boolean estado;

	public String getNombreCompleto() {
		return String.format("%s %s", nombre, apellido);
	}
}
