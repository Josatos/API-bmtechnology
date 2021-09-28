package com.bmtechnology.api.service;

import java.util.List;

import com.bmtechnology.api.interfaceService.IUsuarioService;
import com.bmtechnology.api.model.Usuario;
import com.bmtechnology.api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return (List<Usuario>)usuarioRepository.findAll();
    }

    @Override
    public Page<Usuario> paginacionUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Usuario guardarUsuario(Usuario u) {
        return usuarioRepository.save(u);
    }

    @Override
    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscaUsuario(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }      
}
