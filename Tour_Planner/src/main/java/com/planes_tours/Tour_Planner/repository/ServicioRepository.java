package com.planes_tours.Tour_Planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planes_tours.Tour_Planner.model.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

	List<Servicio> findAllByOrderByIdServicioDesc();
}
