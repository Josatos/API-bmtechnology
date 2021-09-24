package com.bmtechnology.api.repository;

import com.bmtechnology.api.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    
}
