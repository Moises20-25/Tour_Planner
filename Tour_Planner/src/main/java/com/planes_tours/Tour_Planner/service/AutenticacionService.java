package com.planes_tours.Tour_Planner.service;

import org.springframework.stereotype.Service;

import com.planes_tours.Tour_Planner.dto.AutenticacionFilter;
import com.planes_tours.Tour_Planner.model.Usuario;
import com.planes_tours.Tour_Planner.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

	private final UsuarioRepository usuarioRepository;

	public Usuario autenticar(AutenticacionFilter filter) {
		return usuarioRepository.findByEmailAndContrasena(filter.email(), filter.contrasena());
	}
}
