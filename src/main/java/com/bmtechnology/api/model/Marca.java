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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "marcas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Marca implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_marca;

    @Column(name = "nombre", unique = true)
    private String nombre_marca;

    @Column(name = "foto")
    private String foto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "marca", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","marca"})
    private List<Producto> productos;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "marcas_proveedores", joinColumns = @JoinColumn(name="id_marca"),
    inverseJoinColumns = @JoinColumn(name="id_proveedor"),
    uniqueConstraints = {@UniqueConstraint(columnNames = {"id_marca","id_proveedor"})})
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","marca"})
    private List<Proveedor> proveedores;

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
}
