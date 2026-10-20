package com.planes_tours.Tour_Planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planes_tours.Tour_Planner.dto.AutenticacionFilter;
import com.planes_tours.Tour_Planner.service.AutenticacionService;
import com.planes_tours.Tour_Planner.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final AutenticacionService autenticacionService;

	@GetMapping("login")
	public String login(Model model) {
		model.addAttribute("filter", new AutenticacionFilter(null, null));
		return "login";
	}

	@PostMapping("iniciar-sesion")
	public String iniciarSesion(@ModelAttribute AutenticacionFilter filter, HttpSession session,
			RedirectAttributes flash) {

		var usuario = autenticacionService.autenticar(filter);

		// validamos que el usuario exista
		if (usuario == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertError("Email o contraseña incorrectos"));
			return "redirect:/login";
		}

		// validamos que el usuario esté activo
		if (!Boolean.TRUE.equals(usuario.getEstado())) {
			flash.addFlashAttribute("alert", Alert.sweetAlertError("Usuario inactivo, comuníquese con soporte"));
			return "redirect:/login";
		}

		session.setAttribute("fullName", usuario.getNombreCompleto());
		session.setAttribute("tipo", usuario.getTipo());
		session.setAttribute("idUsuario", usuario.getIdUsuario());

		flash.addFlashAttribute("alert",
				Alert.sweetToast("Bienvenido, " + usuario.getNombreCompleto(), "success", 3000));

		return "admin".equals(usuario.getTipo()) ? "redirect:/paquete/listado" : "redirect:/";
	}

	@GetMapping("cerrar-sesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}