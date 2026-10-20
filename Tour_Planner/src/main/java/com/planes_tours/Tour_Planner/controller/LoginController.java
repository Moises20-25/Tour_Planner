package com.planes_tours.Tour_Planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planes_tours.Tour_Planner.dto.AutenticacionFilter;
import com.planes_tours.Tour_Planner.service.AutenticacionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final AutenticacionService autenticacionService;

	@GetMapping("login")
	public String login() {
		return "login";
	}

	@PostMapping("iniciar-sesion")
	public String iniciarSesion(@ModelAttribute AutenticacionFilter filter, HttpSession session,
			RedirectAttributes flash) {
		var usuario = autenticacionService.autenticar(filter);

		if (usuario == null) {
			flash.addFlashAttribute("error", "Email o contraseña incorrectos");
			return "redirect:/login";
		}
		if (!Boolean.TRUE.equals(usuario.getEstado())) {
			flash.addFlashAttribute("error", "Usuario inactivo");
			return "redirect:/login";
		}

		session.setAttribute("fullName", usuario.getNombreCompleto());
		session.setAttribute("tipo", usuario.getTipo());
		session.setAttribute("idUsuario", usuario.getIdUsuario());
		

		return "admin".equals(usuario.getTipo()) ? "redirect:/paquete/listado" : "redirect:/";
	}

	@GetMapping("cerrar-sesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
