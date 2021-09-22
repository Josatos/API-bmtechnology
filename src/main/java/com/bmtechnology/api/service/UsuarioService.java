package com.bmtechnology.api.service;

import com.bmtechnology.api.interfaceService.IUsuarioService;
import com.bmtechnology.api.model.Usuario;
import com.bmtechnology.api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
}
