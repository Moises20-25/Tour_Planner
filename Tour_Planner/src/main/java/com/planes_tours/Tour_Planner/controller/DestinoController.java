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

import com.planes_tours.Tour_Planner.model.Destino;
import com.planes_tours.Tour_Planner.service.DestinoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("destino")
@RequiredArgsConstructor
public class DestinoController {

	private final DestinoService destinoService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstDestinos", destinoService.getAll());
		return "destino/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("destino", new Destino());
		return "destino/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Destino destino, RedirectAttributes flash) {
		var response = destinoService.create(destino);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/destino/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {
		var destino = destinoService.getOne(id);
		if (destino == null) {
			flash.addFlashAttribute("error", "No se encontró el destino");
			return "redirect:/destino/listado";
		}
		model.addAttribute("destino", destino);
		return "destino/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Destino destino, RedirectAttributes flash) {
		var response = destinoService.update(destino);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/destino/listado";
	}

	@PostMapping("cambiar-activo")
	public String cambiarActivo(@RequestParam Integer id, RedirectAttributes flash) {
		var response = destinoService.changeActive(id);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/destino/listado";
	}
}