package com.planes_tours.Tour_Planner.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaqueteSeleccionado {

	private Integer idPaquete;
	private String nombre;
	private String categoria;
	private Integer dias;
	private BigDecimal precio;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaViaje;

	private Integer viajeros;

	// Último día ocupado: el día de inicio cuenta como el primero
	public LocalDate getFechaFin() {
		if (fechaViaje == null || dias == null) {
			return null;
		}
		return fechaViaje.plusDays(dias - 1L);
	}

	public BigDecimal getSubtotal() {
		if (precio == null || viajeros == null) {
			return BigDecimal.ZERO;
		}
		return precio.multiply(BigDecimal.valueOf(viajeros));
	}
}