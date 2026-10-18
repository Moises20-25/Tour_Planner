package com.planes_tours.Tour_Planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planes_tours.Tour_Planner.dto.PaqueteFilter;
import com.planes_tours.Tour_Planner.dto.ResultadoResponse;
import com.planes_tours.Tour_Planner.model.Paquete;
import com.planes_tours.Tour_Planner.repository.PaqueteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaqueteService {

	private final PaqueteRepository paqueteRepository;

	public List<Paquete> search(PaqueteFilter filter) {
		return paqueteRepository.findByFilters(filter.region(), filter.categoria(), filter.idDestino());
	}

	public Paquete getOne(Integer id) {
		return paqueteRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse create(Paquete paquete) {
		try {
			var registrado = paqueteRepository.saveAndFlush(paquete);
			var mensaje = String.format("Paquete con Id %d registrado", registrado.getIdPaquete());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse update(Paquete paquete) {
		try {
			var registrado = paqueteRepository.saveAndFlush(paquete);
			var mensaje = String.format("Paquete con Id %d actualizado", registrado.getIdPaquete());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse delete(Integer id) {
		try {
			paqueteRepository.deleteById(id);
			paqueteRepository.flush();
			return new ResultadoResponse(true, String.format("Paquete con Id %d eliminado", id));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "No se pudo eliminar el paquete");
		}
	}
}