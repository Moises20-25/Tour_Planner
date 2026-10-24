package com.planes_tours.Tour_Planner.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.planes_tours.Tour_Planner.dto.ReservaFilter;
import com.planes_tours.Tour_Planner.dto.ResultadoResponse;
import com.planes_tours.Tour_Planner.model.DetalleReserva;
import com.planes_tours.Tour_Planner.model.Reserva;
import com.planes_tours.Tour_Planner.repository.ReservaRepository;
import com.planes_tours.Tour_Planner.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final ReservaRepository reservaRepository;
	private final UsuarioRepository usuarioRepository;

	public List<Reserva> search(Integer idUsuario, String estado) {
		var todas = reservaRepository.findByFilters(idUsuario); // vienen de la más nueva a la más antigua

		var contador = new HashMap<Integer, Integer>();
		for (int i = todas.size() - 1; i >= 0; i--) {
			var r = todas.get(i);
			r.setNumero(contador.merge(r.getUsuario().getIdUsuario(), 1, Integer::sum));
		}

		if (estado == null || estado.isBlank()) {
			return todas;
		}
		return todas.stream().filter(r -> estado.equals(r.getEstado())).toList();
	}

	
	public List<Reserva> search(ReservaFilter filter) {
		LocalDate desde = filter.desde() != null ? filter.desde() : LocalDate.of(2000, 1, 1);
		LocalDate hasta = filter.hasta() != null ? filter.hasta() : LocalDate.of(2100, 1, 1);
		return reservaRepository.findForReport(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay(),
				filter.estado());
	}

	
	public boolean hayCruce(Integer idUsuario, LocalDate ini, LocalDate fin) {
		return reservaRepository.contarCruces(idUsuario, ini, fin) > 0;
	}

	
	@Transactional
	public ResultadoResponse create(Reserva reserva, Integer idUsuario) {
		try {
			var usuario = usuarioRepository.findById(idUsuario).orElse(null);
			if (usuario == null) {
				return new ResultadoResponse(false, "No se encontró el usuario");
			}

			var detalles = reserva.getDetalles();
			var manana = LocalDate.now().plusDays(1);

			for (int i = 0; i < detalles.size(); i++) {
				var det = detalles.get(i);
				var ini = det.getFechaInicio();
				var fin = det.getFechaFin();
				var nombre = det.getPaquete().getNombre();

				if (ini == null || ini.isBefore(manana)) {
					return new ResultadoResponse(false, "La fecha de viaje de «" + nombre + "» debe ser desde mañana");
				}
				if (det.getCantidad() == null || det.getCantidad() < 1) {
					return new ResultadoResponse(false, "Los viajeros de «" + nombre + "» deben ser al menos 1");
				}
				// cruce con reservas ya registradas del cliente (las canceladas no cuentan)
				if (hayCruce(idUsuario, ini, fin)) {
					return new ResultadoResponse(false, "Ya tienes una reserva entre el " + ini.format(FMT)
							+ " y el " + fin.format(FMT) + ". Elige otras fechas");
				}
				// cruce con los otros paquetes de esta misma reserva
				for (int j = 0; j < i; j++) {
					var otro = detalles.get(j);
					if (!ini.isAfter(otro.getFechaFin()) && !fin.isBefore(otro.getFechaInicio())) {
						return new ResultadoResponse(false,
								"Los paquetes elegidos se cruzan en fechas. Cambia alguno de ellos");
					}
				}
			}

			
			var total = detalles.stream().map(DetalleReserva::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
			var pasajeros = detalles.stream().mapToInt(DetalleReserva::getCantidad).sum();
			var inicio = detalles.stream().map(DetalleReserva::getFechaInicio).min(Comparator.naturalOrder()).get();
			var fin = detalles.stream().map(DetalleReserva::getFechaFin).max(Comparator.naturalOrder()).get();
			var codigo = "RES-" + System.currentTimeMillis();

			reserva.setUsuario(usuario);
			reserva.setFechaReserva(LocalDateTime.now());
			reserva.setFechaInicio(inicio);
			reserva.setFechaFin(fin);
			reserva.setEstado("confirmada");
			reserva.setMontoTotal(total);
			reserva.setPasajeros(pasajeros);
			reserva.setCodigoReserva(codigo);

			
			var registrada = reservaRepository.saveAndFlush(reserva);

			long numero = reservaRepository.countByUsuario_IdUsuarioAndIdReservaLessThanEqual(idUsuario,
					registrada.getIdReserva());

			return new ResultadoResponse(true, "Reserva " + numero + " confirmada con éxito");
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return new ResultadoResponse(false, "No se pudo registrar la reserva. No se guardó ningún cambio");
		}
	}

	@Transactional
	public ResultadoResponse cancel(Integer idReserva, Integer idUsuario, boolean esAdmin) {
		try {
			var reserva = reservaRepository.findById(idReserva).orElse(null);
			if (reserva == null) {
				return new ResultadoResponse(false, "No se encontró la reserva");
			}
			if (!esAdmin && !reserva.getUsuario().getIdUsuario().equals(idUsuario)) {
				return new ResultadoResponse(false, "No puedes cancelar la reserva de otro cliente");
			}
			if ("cancelada".equals(reserva.getEstado())) {
				return new ResultadoResponse(false, "La reserva ya estaba cancelada");
			}

			reserva.setEstado("cancelada");
			reservaRepository.saveAndFlush(reserva);

			return new ResultadoResponse(true,
					String.format("Reserva %s cancelada. Esas fechas quedaron libres", reserva.getCodigoReserva()));
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return new ResultadoResponse(false, "No se pudo cancelar la reserva");
		}
	}
}