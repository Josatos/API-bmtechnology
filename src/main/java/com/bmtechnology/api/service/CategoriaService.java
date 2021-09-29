package com.bmtechnology.api.service;

import java.util.List;

import com.bmtechnology.api.interfaceService.ICategoriaService;
import com.bmtechnology.api.model.Categoria;
import com.bmtechnology.api.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    CategoriaRepository cRepository;

    @Override
    public List<Categoria> listarCategorias() {
        return (List<Categoria>) cRepository.findAll();
    }

    @Override
    public Categoria guardaCategoria(Categoria c) { 
        return cRepository.save(c);
    }

    @Override
    public void eliminarCategoria(int id) {
        cRepository.deleteById(id);
    }

    @Override
    public Categoria buscarCategoria(int id) {
        return cRepository.findById(id).orElse(null);
    }

}
