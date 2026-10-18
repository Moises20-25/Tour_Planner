package com.planes_tours.Tour_Planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.planes_tours.Tour_Planner.model.Destino;

public interface DestinoRepository extends JpaRepository<Destino, Integer> {

	List<Destino> findAllByOrderByIdDestinoDesc();

	@Query("""
			select d from Destino d
			where d.estado = true
			  and (:filtro is null or :filtro = ''
			       or lower(d.nombre) like lower(concat('%', :filtro, '%'))
			       or lower(d.pais) like lower(concat('%', :filtro, '%'))
			       or lower(d.ciudad) like lower(concat('%', :filtro, '%')))
			  and (:categoria is null or :categoria = '' or d.categoria = :categoria)
			order by d.popularidad desc, d.nombre
			""")
	List<Destino> findByFilters(@Param("filtro") String filtro, @Param("categoria") String categoria);
}
