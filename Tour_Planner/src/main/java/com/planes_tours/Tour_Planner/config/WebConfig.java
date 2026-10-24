package com.planes_tours.Tour_Planner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Solo administrador
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/destino/**", "/paquete/**", "/servicio/**");

		// Cualquier usuario logueado (cliente o admin)
		registry.addInterceptor(new SesionInterceptor())
				.addPathPatterns("/reserva/**", "/reporte/**");
	}
}