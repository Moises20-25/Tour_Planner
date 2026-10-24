package com.planes_tours.Tour_Planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.planes_tours.Tour_Planner.dto.DestinoFilter;
import com.planes_tours.Tour_Planner.dto.PaqueteFilter;
import com.planes_tours.Tour_Planner.service.DestinoService;
import com.planes_tours.Tour_Planner.service.PaqueteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

	private final DestinoService destinoService;
	private final PaqueteService paqueteService;

	@GetMapping("/destinos")
	public String destinos(@ModelAttribute DestinoFilter filter, Model model) {
		model.addAttribute("destinos", destinoService.search(filter));
		model.addAttribute("filtro", filter.filtro());
		model.addAttribute("categoriaSeleccionada", filter.categoria());
		return "destinos";
	}

	@GetMapping("/paquetes")
	public String paquetes(@ModelAttribute PaqueteFilter filter, Model model) {
		model.addAttribute("paquetes", paqueteService.search(filter));
		model.addAttribute("regionSeleccionada", filter.region());
		model.addAttribute("categoriaSeleccionada", filter.categoria());
		return "paquetes";
	}

	// Botón "Reservar" de cada paquete: abre la emisión de reserva con ese paquete cargado
	@GetMapping("/reservar/{id}")
	public String reservar(@PathVariable Integer id) {
		return "redirect:/reserva/nuevo?idPaquete=" + id;
	}
}
