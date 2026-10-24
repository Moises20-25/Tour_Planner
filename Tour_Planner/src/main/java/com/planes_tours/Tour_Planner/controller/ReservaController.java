package com.planes_tours.Tour_Planner.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planes_tours.Tour_Planner.dto.PaqueteSeleccionado;
import com.planes_tours.Tour_Planner.model.DetalleReserva;
import com.planes_tours.Tour_Planner.model.Reserva;
import com.planes_tours.Tour_Planner.service.PaqueteService;
import com.planes_tours.Tour_Planner.service.ReservaService;
import com.planes_tours.Tour_Planner.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("reserva")
@RequiredArgsConstructor
@SessionAttributes({ "seleccionados" })
public class ReservaController {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final ReservaService reservaService;
	private final PaqueteService paqueteService;

	// Inicializamos la lista de paquetes seleccionados en sesión
	@ModelAttribute("seleccionados")
	public List<PaqueteSeleccionado> inicio() {
		return new ArrayList<PaqueteSeleccionado>();
	}

	@GetMapping("listado")
	public String listado(@RequestParam(required = false) String estado, HttpSession session, Model model) {
		var tipo = (String) session.getAttribute("tipo");
		Integer idUsuario = null; // administrador: sin filtro, ve todas
		if (!"admin".equals(tipo)) {
			idUsuario = (Integer) session.getAttribute("idUsuario"); // los demás: solo las suyas
		}
		model.addAttribute("lstReservas", reservaService.search(idUsuario, estado));
		model.addAttribute("estadoSel", estado);
		return "reserva/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(@RequestParam(required = false) Integer idPaquete,
			@ModelAttribute("seleccionados") List<PaqueteSeleccionado> seleccionados, Model model) {

		var paqueteSeleccionado = new PaqueteSeleccionado();
		paqueteSeleccionado.setViajeros(1);

		// Si viene desde el botón "Reservar" de un paquete, lo dejamos cargado
		if (idPaquete != null) {
			var p = paqueteService.getOne(idPaquete);
			if (p != null) {
				paqueteSeleccionado.setIdPaquete(p.getIdPaquete());
				paqueteSeleccionado.setNombre(p.getNombre());
				paqueteSeleccionado.setCategoria(p.getCategoria());
				paqueteSeleccionado.setDias(p.getDias());
				paqueteSeleccionado.setPrecio(p.getPrecio());
			}
		}

		var total = seleccionados.stream().map(PaqueteSeleccionado::getSubtotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		model.addAttribute("paqueteSeleccionado", paqueteSeleccionado);
		model.addAttribute("paquetes", paqueteService.getAll());
		model.addAttribute("total", total);
		model.addAttribute("manana", LocalDate.now().plusDays(1));
		return "reserva/nuevo";
	}

	@PostMapping("agregar")
	public String agregar(@ModelAttribute PaqueteSeleccionado paqueteSeleccionado,
			@ModelAttribute("seleccionados") List<PaqueteSeleccionado> seleccionados,
			HttpSession session, RedirectAttributes flash) {

		var idUsuario = (Integer) session.getAttribute("idUsuario");
		var paquete = paqueteSeleccionado.getIdPaquete() == null ? null
				: paqueteService.getOne(paqueteSeleccionado.getIdPaquete());

		if (paquete == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Seleccione un lugar"));
			return "redirect:/reserva/nuevo";
		}

		// volvemos a la misma pantalla con el paquete ya elegido
		var volver = "redirect:/reserva/nuevo?idPaquete=" + paquete.getIdPaquete();

		// validamos si ya existe el paquete en la lista
		boolean existe = seleccionados.stream()
				.anyMatch(p -> p.getIdPaquete().equals(paqueteSeleccionado.getIdPaquete()));

		if (existe) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("El paquete se encuentra en la lista"));
			return "redirect:/reserva/nuevo";
		}

		// validamos la fecha: solo desde mañana
		var manana = LocalDate.now().plusDays(1);
		if (paqueteSeleccionado.getFechaViaje() == null || paqueteSeleccionado.getFechaViaje().isBefore(manana)) {
			flash.addFlashAttribute("alert",
					Alert.sweetAlertInfo("La fecha de viaje debe ser desde mañana (" + manana.format(FMT) + ")"));
			return volver;
		}

		// validamos los viajeros
		if (paqueteSeleccionado.getViajeros() == null || paqueteSeleccionado.getViajeros() < 1) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("El número de viajeros debe ser al menos 1"));
			return volver;
		}

		// Los datos del paquete se leen de la base de datos, no del formulario
		paqueteSeleccionado.setNombre(paquete.getNombre());
		paqueteSeleccionado.setCategoria(paquete.getCategoria());
		paqueteSeleccionado.setDias(paquete.getDias() == null || paquete.getDias() < 1 ? 1 : paquete.getDias());
		paqueteSeleccionado.setPrecio(paquete.getPrecio());

		var ini = paqueteSeleccionado.getFechaViaje();
		var fin = paqueteSeleccionado.getFechaFin();

		// 1) cruce con los paquetes ya agregados a esta reserva
		for (var s : seleccionados) {
			if (!ini.isAfter(s.getFechaFin()) && !fin.isBefore(s.getFechaViaje())) {
				flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Esas fechas se cruzan con «" + s.getNombre()
						+ "» (" + s.getFechaViaje().format(FMT) + " al " + s.getFechaFin().format(FMT) + ")"));
				return volver;
			}
		}

		// 2) cruce con reservas que el cliente ya registró
		if (reservaService.hayCruce(idUsuario, ini, fin)) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Ya tienes una reserva entre el "
					+ ini.format(FMT) + " y el " + fin.format(FMT) + ". Elige otras fechas"));
			return volver;
		}

		seleccionados.add(paqueteSeleccionado);
		return "redirect:/reserva/nuevo";
	}

	@PostMapping("remover")
	public String remover(@RequestParam Integer id,
			@ModelAttribute("seleccionados") List<PaqueteSeleccionado> seleccionados) {

		// Removemos el paquete de la lista de paquetes seleccionados
		seleccionados.removeIf(p -> p.getIdPaquete().equals(id));

		return "redirect:/reserva/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Reserva reserva,
			@ModelAttribute("seleccionados") List<PaqueteSeleccionado> seleccionados,
			RedirectAttributes flash, HttpSession session, SessionStatus sessionStatus) {

		// Obtenemos dato de sesión
		var idUsuario = (Integer) session.getAttribute("idUsuario");

		// Validamos sesión
		if (idUsuario == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Sesión caducada"));
			return "redirect:/login";
		}

		// Validamos que al menos haya un seleccionado
		if (seleccionados.isEmpty()) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Agregue como min 1 paquete"));
			return "redirect:/reserva/nuevo";
		}

		// Mapeamos los datos seleccionados a DetalleReserva y lo seteamos
		var detalles = seleccionados.stream().map(item -> {
			var paquete = paqueteService.getOne(item.getIdPaquete());
			var det = new DetalleReserva();
			det.setReserva(reserva);
			det.setPaquete(paquete);
			det.setFechaInicio(item.getFechaViaje());
			det.setFechaFin(item.getFechaFin());
			det.setCantidad(item.getViajeros());
			det.setPrecioUnitario(paquete.getPrecio());
			det.setSubtotal(paquete.getPrecio().multiply(BigDecimal.valueOf(item.getViajeros())));
			return det;
		}).toList();

		// Seteamos el listado del detalle
		reserva.setDetalles(new ArrayList<>(detalles));

		// Registramos finalmente la reserva
		var response = reservaService.create(reserva, idUsuario);

		if (!response.success()) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo(response.mensaje()));
			return "redirect:/reserva/nuevo";
		}

		sessionStatus.setComplete(); // vacía la lista de la sesión

		// Enviamos mensaje de éxito
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/reserva/listado";
	}

	@PostMapping("cancelar")
	public String cancelar(@RequestParam Integer idReserva, HttpSession session, RedirectAttributes flash) {
		var idUsuario = (Integer) session.getAttribute("idUsuario");
		var esAdmin = "admin".equals(session.getAttribute("tipo"));

		var response = reservaService.cancel(idReserva, idUsuario, esAdmin);
		flash.addFlashAttribute("alert",
				response.success() ? Alert.sweetToast(response.mensaje(), "success", 5000)
						: Alert.sweetAlertInfo(response.mensaje()));
		return "redirect:/reserva/listado";
	}
}