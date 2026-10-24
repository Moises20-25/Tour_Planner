package com.planes_tours.Tour_Planner.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SesionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(true);

		if (session.getAttribute("idUsuario") == null) {
			// Guardamos a dónde iba para volver después del login
			if ("GET".equals(request.getMethod())) {
				String ruta = request.getRequestURI().substring(request.getContextPath().length());
				if (request.getQueryString() != null) {
					ruta += "?" + request.getQueryString();
				}
				session.setAttribute("urlPendiente", ruta);
			}
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		return true;
	}
}
