package com.planes_tours.Tour_Planner.model;

import java.math.BigDecimal;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paquetes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Paquete {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idPaquete")
	private Integer idPaquete;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "precio")
	private BigDecimal precio;

	@Column(name = "precioOriginal")
	private BigDecimal precioOriginal;

	@Column(name = "descuento")
	private Boolean descuento;

	@Column(name = "region")
	private String region;

	@Column(name = "categoria")
	private String categoria;

	@Column(name = "dias")
	private Integer dias;

	@Column(name = "imagen")
	private String imagen;

	@Column(name = "viajeros")
	private Integer viajeros;

	@Column(name = "destacado")
	private Boolean destacado;

	@ManyToOne
	@JoinColumn(name = "idDestino")
	private Destino destino;
}