package com.planes_tours.Tour_Planner.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.planes_tours.Tour_Planner.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

	@Query("""
			select distinct r from Reserva r
			join fetch r.usuario
			join fetch r.detalles d
			left join fetch d.paquete
			left join fetch d.servicio
			where r.fechaReserva >= :desde
			  and r.fechaReserva < :hasta
			  and (:estado is null or :estado = '' or r.estado = :estado)
			order by r.idReserva desc
			""")
	List<Reserva> findForReport(@Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta,
			@Param("estado") String estado);

	
	@Query("""
			select distinct r from Reserva r
			join fetch r.usuario u
			join fetch r.detalles d
			left join fetch d.paquete
			left join fetch d.servicio
			where (:idUsuario is null or u.idUsuario = :idUsuario)
			order by r.idReserva desc
			""")
	List<Reserva> findByFilters(@Param("idUsuario") Integer idUsuario);

	
	@Query("""
			select count(d) from DetalleReserva d
			join d.reserva r
			where r.usuario.idUsuario = :idUsuario
			  and r.estado <> 'cancelada'
			  and coalesce(d.fechaInicio, r.fechaInicio) <= :fin
			  and coalesce(d.fechaFin, r.fechaFin) >= :ini
			""")
	long contarCruces(@Param("idUsuario") Integer idUsuario,
			@Param("ini") LocalDate ini,
			@Param("fin") LocalDate fin);
	
	
	long countByUsuario_IdUsuarioAndIdReservaLessThanEqual(Integer idUsuario, Integer idReserva);
}