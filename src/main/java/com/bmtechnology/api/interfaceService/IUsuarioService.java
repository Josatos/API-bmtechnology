package com.bmtechnology.api.interfaceService;

import com.bmtechnology.api.model.Usuario;

public interface IUsuarioService {
    public Usuario findByEmail(String email);
}
