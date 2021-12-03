package com.egg.social.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Perfil implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String residencia;
    @Temporal(TemporalType.DATE)
    private Date fechaDeBaja;
    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> tecnologias;
    @OneToOne
    private Usuario usuario;
    @OneToMany(mappedBy = "perfil")
    private List<Publicacion> publicaciones;
    @OneToMany(mappedBy = "remitente")
    private List<Invitacion> invitacionesRecibidas;
    @OneToMany(mappedBy = "destinatario")
    private List<Invitacion> invitacionesEnviadas;
    @Lob
    private String foto;

    public Perfil() {
        tecnologias = new ArrayList<>();
        publicaciones = new ArrayList<>();
        invitacionesRecibidas = new ArrayList<>();
        invitacionesEnviadas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaDeBaja() {
        return fechaDeBaja;
    }

    public void setFechaDeBaja(Date fechaDeBaja) {
        this.fechaDeBaja = fechaDeBaja;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public List<String> getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(List<String> tecnologias) {
        this.tecnologias = tecnologias;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<Invitacion> getInvitacionesRecibidas() {
        return invitacionesRecibidas;
    }

    public void setInvitacionesRecibidas(List<Invitacion> invitacionesRecibidas) {
        this.invitacionesRecibidas = invitacionesRecibidas;
    }

    public List<Invitacion> getInvitacionesEnviadas() {
        return invitacionesEnviadas;
    }

    public void setInvitacionesEnviadas(List<Invitacion> invitacionesEnviadas) {
        this.invitacionesEnviadas = invitacionesEnviadas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
