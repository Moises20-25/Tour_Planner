package com.planes_tours.Tour_Planner.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		boolean esAdmin = session != null && "admin".equals(session.getAttribute("tipo"));

		if (!esAdmin) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		return true;
	}
}