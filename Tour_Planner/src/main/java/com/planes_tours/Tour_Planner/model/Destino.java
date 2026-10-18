package com.planes_tours.Tour_Planner.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Destino {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDestino")
	private Integer idDestino;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "pais")
	private String pais;

	@Column(name = "ciudad")
	private String ciudad;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "categoria")
	private String categoria;

	@Column(name = "popularidad")
	private Integer popularidad;

	@Column(name = "imagenPrincipal")
	private String imagenPrincipal;

	@Column(name = "estado")
	private Boolean estado;

	public String getEstadoDescripcion() {
		return Boolean.TRUE.equals(estado) ? "Activo" : "Inactivo";
	}
}