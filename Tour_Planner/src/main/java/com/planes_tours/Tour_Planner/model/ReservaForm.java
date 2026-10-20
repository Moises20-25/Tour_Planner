package com.planes_tours.Tour_Planner.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaForm {

    private String nombre;

    private String correo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaViaje;

    private Integer viajeros = 1;
}