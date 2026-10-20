package com.planes_tours.Tour_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planes_tours.Tour_Planner.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
}