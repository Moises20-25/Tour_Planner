package com.planes_tours.Tour_Planner.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idReserva")
	private Integer idReserva;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@Column(name = "fechaReserva")
	private LocalDateTime fechaReserva;

	@Column(name = "fechaInicio")
	private LocalDate fechaInicio;

	@Column(name = "fechaFin")
	private LocalDate fechaFin;

	@Column(name = "estado")
	private String estado;

	@Column(name = "montoTotal")
	private BigDecimal montoTotal;

	@Column(name = "pasajeros")
	private Integer pasajeros;

	@Column(name = "codigoReserva")
	private String codigoReserva;

	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
	private List<DetalleReserva> detalles;

	
	@Transient
	private Integer numero;
}