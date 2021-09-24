package com.bmtechnology.api.service;

import java.util.List;

import com.bmtechnology.api.interfaceService.IProductoService;
import com.bmtechnology.api.model.Producto;
import com.bmtechnology.api.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService implements IProductoService{

    @Autowired
    private ProductoRepository pRepository;

    @Override
    public List<Producto> listarProductos() {
        return (List<Producto>)pRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto p) {
        return pRepository.save(p);
    }

    @Override
    public void eliminarProducto(int id) {
        pRepository.deleteById(id);
    }

    @Override
    public Producto buscarProducto(int id) {
        return pRepository.findById(id).orElse(null);
    }
    
}
