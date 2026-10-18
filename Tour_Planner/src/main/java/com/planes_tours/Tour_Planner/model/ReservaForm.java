package com.planes_tours.Tour_Planner.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaForm {

	private String nombre;

	private String correo;

	private LocalDate fechaViaje;

	private Integer viajeros = 1;
}
