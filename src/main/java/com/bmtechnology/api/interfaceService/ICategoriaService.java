package com.bmtechnology.api.interfaceService;

import java.util.List;

import com.bmtechnology.api.model.Categoria;

public interface ICategoriaService {
    public List<Categoria> listarCategorias();
    public Categoria guardaCategoria(Categoria c);
    public void eliminarCategoria(int id);
    public Categoria buscarCategoria(int id);
}
