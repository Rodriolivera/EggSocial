package com.egg.social.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Invitacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Perfil remitente;
    @ManyToOne
    private Perfil destinatario;
    private Boolean aceptada;
    @Temporal(TemporalType.DATE)
    private Date fechaDeBaja;

    public Invitacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Perfil getRemitente() {
        return remitente;
    }

    public void setRemitente(Perfil remitente) {
        this.remitente = remitente;
    }

    public Perfil getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Perfil destinatario) {
        this.destinatario = destinatario;
    }

    public Boolean getAceptada() {
        return aceptada;
    }

    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    public Date getFechaDeBaja() {
        return fechaDeBaja;
    }

    public void setFechaDeBaja(Date fechaDeBaja) {
        this.fechaDeBaja = fechaDeBaja;
    }
}
