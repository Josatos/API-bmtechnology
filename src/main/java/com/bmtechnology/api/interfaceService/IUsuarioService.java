package com.bmtechnology.api.interfaceService;

import java.util.List;

import com.bmtechnology.api.model.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {
    public Usuario findByEmail(String email);
    public List<Usuario> listarUsuarios();
    public Page<Usuario> paginacionUsuarios(Pageable pageable);
    public Usuario guardarUsuario(Usuario u);
    public void eliminarUsuario(int id);
    public Usuario buscaUsuario(int id);
}
