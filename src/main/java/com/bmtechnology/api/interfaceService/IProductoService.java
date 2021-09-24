package com.bmtechnology.api.interfaceService;

import java.util.List;

import com.bmtechnology.api.model.Producto;

public interface IProductoService {
    public List<Producto> listarProductos();
    public Producto guardarProducto(Producto p);
    public void eliminarProducto(int id);
    public Producto buscarProducto(int id);
}
