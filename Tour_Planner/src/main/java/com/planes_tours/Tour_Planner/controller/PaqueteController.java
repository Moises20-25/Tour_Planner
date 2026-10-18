package com.planes_tours.Tour_Planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planes_tours.Tour_Planner.dto.PaqueteFilter;
import com.planes_tours.Tour_Planner.model.Paquete;
import com.planes_tours.Tour_Planner.service.DestinoService;
import com.planes_tours.Tour_Planner.service.PaqueteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("paquete")
@RequiredArgsConstructor
public class PaqueteController {

	private final PaqueteService paqueteService;
	private final DestinoService destinoService;

	@GetMapping("listado")
	public String listado(@ModelAttribute PaqueteFilter filter, Model model) {
		model.addAttribute("lstPaquetes", paqueteService.search(filter));
		model.addAttribute("lstDestinos", destinoService.getAll());
		model.addAttribute("filter", filter);
		return "paquete/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("lstDestinos", destinoService.getAll());
		model.addAttribute("paquete", new Paquete());
		return "paquete/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Paquete paquete, RedirectAttributes flash) {
		var response = paqueteService.create(paquete);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/paquete/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {
		var paquete = paqueteService.getOne(id);
		if (paquete == null) {
			flash.addFlashAttribute("error", "No se encontró el paquete");
			return "redirect:/paquete/listado";
		}
		model.addAttribute("lstDestinos", destinoService.getAll());
		model.addAttribute("paquete", paquete);
		return "paquete/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Paquete paquete, RedirectAttributes flash) {
		var response = paqueteService.update(paquete);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/paquete/listado";
	}

	@PostMapping("eliminar")
	public String eliminar(@RequestParam Integer id, RedirectAttributes flash) {
		var response = paqueteService.delete(id);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/paquete/listado";
	}
}
