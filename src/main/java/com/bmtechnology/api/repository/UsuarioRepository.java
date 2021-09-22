package com.bmtechnology.api.repository;

import com.bmtechnology.api.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    public Usuario findByEmail(String email);
}
