package com.planes_tours.Tour_Planner.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "detalle_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDetalle")
	private Integer idDetalle;

	@ManyToOne
	@JoinColumn(name = "idReserva")
	private Reserva reserva;

	@ManyToOne
	@JoinColumn(name = "idServicio")
	private Servicio servicio;

	@ManyToOne
	@JoinColumn(name = "idPaquete")
	private Paquete paquete;

	@Column(name = "cantidad")
	private Integer cantidad;

	@Column(name = "precioUnitario")
	private BigDecimal precioUnitario;

	@Column(name = "subtotal")
	private BigDecimal subtotal;

	@Column(name = "fechaInicio")
	private LocalDate fechaInicio;

	@Column(name = "fechaFin")
	private LocalDate fechaFin;
}