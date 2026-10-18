package com.planes_tours.Tour_Planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.planes_tours.Tour_Planner.model.Paquete;

public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {

	@Query("""
			select p from Paquete p
			where (:region is null or :region = '' or p.region = :region)
			  and (:categoria is null or :categoria = '' or p.categoria = :categoria)
			  and (:idDestino is null or p.destino.idDestino = :idDestino)
			order by p.idPaquete desc
			""")
	List<Paquete> findByFilters(@Param("region") String region,
			@Param("categoria") String categoria,
			@Param("idDestino") Integer idDestino);
}
