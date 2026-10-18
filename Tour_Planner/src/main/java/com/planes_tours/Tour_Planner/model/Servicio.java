package com.planes_tours.Tour_Planner.model;

import java.math.BigDecimal;

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
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Servicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idServicio")
	private Integer idServicio;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "precio")
	private BigDecimal precio;

	@Column(name = "stock")
	private Integer stock;

	@Column(name = "imagen")
	private String imagen;

	@Column(name = "disponibilidad")
	private Boolean disponibilidad;

	@Column(name = "idProveedor")
	private String idProveedor;

	public String getDisponibilidadDescripcion() {
		return Boolean.TRUE.equals(disponibilidad) ? "Disponible" : "No disponible";
	}
}
