package com.bmtechnology.api.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @Column(unique = true, length = 60)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(length = 80)
    private String nombres;

    @Column(length = 80)
    private String apellidos;

    @Column(unique = true, length = 8)
    private String dni;

    @Column(length = 20)
    private String telefono;

    @Column(name = "foto")
    private String foto;

    private Boolean activo;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fecha_creacion;

    @PrePersist
    public void prePersist() {
        fecha_creacion = new Date();
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name="id_usuario"),
    inverseJoinColumns = @JoinColumn(name="id_rol"),
    uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuario","id_rol"})})
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","usuarios"})
    private List<Rol> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","usuario"})
    private List<PedidoVenta> pedidos;

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
}
