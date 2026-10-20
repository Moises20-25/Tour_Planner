package com.planes_tours.Tour_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planes_tours.Tour_Planner.model.DetalleReserva;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, Integer> {
}