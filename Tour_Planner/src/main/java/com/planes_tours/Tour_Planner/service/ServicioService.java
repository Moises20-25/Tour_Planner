package com.planes_tours.Tour_Planner.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planes_tours.Tour_Planner.dto.ResultadoResponse;
import com.planes_tours.Tour_Planner.model.Servicio;
import com.planes_tours.Tour_Planner.repository.ServicioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioService {

	private final ServicioRepository servicioRepository;

	public List<Servicio> getAll() {
		return servicioRepository.findAllByOrderByIdServicioDesc();
	}

	public Servicio getOne(Integer id) {
		return servicioRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse create(Servicio servicio) {
		try {
			var registrado = servicioRepository.saveAndFlush(servicio);
			var mensaje = String.format("Servicio con Id %d registrado", registrado.getIdServicio());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse update(Servicio servicio) {
		try {
			var registrado = servicioRepository.saveAndFlush(servicio);
			var mensaje = String.format("Servicio con Id %d actualizado", registrado.getIdServicio());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse changeActive(Integer id) {
		try {
			var servicio = servicioRepository.findById(id).orElse(null);
			if (servicio == null) {
				return new ResultadoResponse(false, "No se encontró el servicio");
			}
			servicio.setDisponibilidad(!Boolean.TRUE.equals(servicio.getDisponibilidad()));
			var accion = servicio.getDisponibilidad() ? "activado" : "desactivado";
			return new ResultadoResponse(true, String.format("Servicio con Id %d %s", id, accion));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
}