package com.planes_tours.Tour_Planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planes_tours.Tour_Planner.dto.DestinoFilter;
import com.planes_tours.Tour_Planner.dto.ResultadoResponse;
import com.planes_tours.Tour_Planner.model.Destino;
import com.planes_tours.Tour_Planner.repository.DestinoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DestinoService {

	private final DestinoRepository destinoRepository;

	public List<Destino> getAll() {
		return destinoRepository.findAllByOrderByIdDestinoDesc();
	}

	public List<Destino> search(DestinoFilter filter) {
		return destinoRepository.findByFilters(filter.filtro(), filter.categoria());
	}

	public Destino getOne(Integer id) {
		return destinoRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse create(Destino destino) {
		try {
			var registrado = destinoRepository.saveAndFlush(destino);
			var mensaje = String.format("Destino con Id %d registrado", registrado.getIdDestino());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse update(Destino destino) {
		try {
			var registrado = destinoRepository.saveAndFlush(destino);
			var mensaje = String.format("Destino con Id %d actualizado", registrado.getIdDestino());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse changeActive(Integer id) {
		try {
			var destino = destinoRepository.findById(id).orElse(null);
			if (destino == null) {
				return new ResultadoResponse(false, "No se encontró el destino");
			}
			destino.setEstado(!Boolean.TRUE.equals(destino.getEstado()));
			var accion = destino.getEstado() ? "activado" : "desactivado";
			return new ResultadoResponse(true, String.format("Destino con Id %d %s", id, accion));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
}