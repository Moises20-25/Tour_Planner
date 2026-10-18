package com.planes_tours.Tour_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planes_tours.Tour_Planner.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByEmailAndContrasena(String email, String contrasena);
}
