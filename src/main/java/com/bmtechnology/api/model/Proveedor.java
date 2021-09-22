package com.bmtechnology.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_proveedor;

    @Column(name = "nom_proovedor", length = 75)
    private String nombre;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "telefono", length = 12)
    private String telefono;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proveedor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","proveedor"})
    private List<Producto> productos;

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
}
