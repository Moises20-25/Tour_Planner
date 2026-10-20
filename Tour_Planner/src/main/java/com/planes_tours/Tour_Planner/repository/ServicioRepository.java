package com.planes_tours.Tour_Planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.planes_tours.Tour_Planner.model.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

	List<Servicio> findAllByOrderByIdServicioDesc();
	
    @Query(value = """
            select s.* from servicios s
            join servicios_paquete sp on sp.idServicio = s.idServicio
            where sp.idPaquete = :idPaquete
            """, nativeQuery = true)
    List<Servicio> findByPaquete(@Param("idPaquete") Integer idPaquete);
	
}
