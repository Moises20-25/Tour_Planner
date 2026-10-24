package com.planes_tours.Tour_Planner.dto;

import java.time.LocalDate;

public record ReservaFilter(LocalDate desde, LocalDate hasta, String estado) {
}