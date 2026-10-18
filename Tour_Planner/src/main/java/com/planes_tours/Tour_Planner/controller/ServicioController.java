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

import com.planes_tours.Tour_Planner.model.Servicio;
import com.planes_tours.Tour_Planner.service.ServicioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("servicio")
@RequiredArgsConstructor
public class ServicioController {

	private final ServicioService servicioService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstServicios", servicioService.getAll());
		return "servicio/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("servicio", new Servicio());
		return "servicio/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Servicio servicio, RedirectAttributes flash) {
		var response = servicioService.create(servicio);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/servicio/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {
		var servicio = servicioService.getOne(id);
		if (servicio == null) {
			flash.addFlashAttribute("error", "No se encontró el servicio");
			return "redirect:/servicio/listado";
		}
		model.addAttribute("servicio", servicio);
		return "servicio/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Servicio servicio, RedirectAttributes flash) {
		var response = servicioService.update(servicio);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/servicio/listado";
	}

	@PostMapping("cambiar-activo")
	public String cambiarActivo(@RequestParam Integer id, RedirectAttributes flash) {
		var response = servicioService.changeActive(id);
		flash.addFlashAttribute(response.success() ? "mensaje" : "error", response.mensaje());
		return "redirect:/servicio/listado";
	}
}
