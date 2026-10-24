package com.planes_tours.Tour_Planner.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planes_tours.Tour_Planner.repository.ReservaRepository;
import com.planes_tours.Tour_Planner.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
@RequiredArgsConstructor
public class ReporteController {

	private final ReporteService reporteService;
	private final ReservaRepository reservaRepository;

	@GetMapping("reserva/{numReserva}")
	public void reserva(@PathVariable Integer numReserva, HttpServletResponse response, HttpSession session)
			throws Exception {

		
		var reserva = reservaRepository.findById(numReserva).orElse(null);
		var tipo = (String) session.getAttribute("tipo");
		var idUsuario = (Integer) session.getAttribute("idUsuario");

		if (reserva == null || (!"admin".equals(tipo) && !reserva.getUsuario().getIdUsuario().equals(idUsuario))) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		
		String reportPath = "/reportes/boleta/boleta.jrxml";

		
		Map<String, Object> params = new HashMap<>();
		params.put("pNumReserva", numReserva);

		
		JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

		
		long numero = reservaRepository.countByUsuario_IdUsuarioAndIdReservaLessThanEqual(
				reserva.getUsuario().getIdUsuario(), numReserva);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", String.format("inline; filename=reserva-nro-%s.pdf", numero));

		
		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

		outputStream.flush();
		outputStream.close();
	}
}