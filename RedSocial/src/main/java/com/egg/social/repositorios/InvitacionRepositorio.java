package com.egg.social.repositorios;

import com.egg.social.entidades.Invitacion;
import com.egg.social.entidades.Perfil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitacionRepositorio extends JpaRepository<Invitacion, Long> {

    @Query("SELECT i.remitente.id FROM Invitacion i WHERE i.aceptada = true AND i.fechaDeBaja IS NULL AND i.destinatario.id = :id")
    List<Long> listaRemitente(@Param("id") Long id);

    @Query("SELECT i.destinatario.id FROM Invitacion i WHERE i.aceptada = true AND i.fechaDeBaja IS NULL AND i.remitente.id = :id")
    List<Long> listaDestinatario(@Param("id") Long id);

    @Query("SELECT i.remitente FROM Invitacion i WHERE i.aceptada = true AND i.fechaDeBaja IS NULL AND i.destinatario.id = :id")
    List<Perfil> listaRemitentePerfil(@Param("id") Long id);

    @Query("SELECT i.destinatario FROM Invitacion i WHERE i.aceptada = true AND i.fechaDeBaja IS NULL AND i.remitente.id = :id")
    List<Perfil> listaDestinatarioPerfil(@Param("id") Long id);

    @Query("SELECT i FROM Invitacion i WHERE i.id = :idInvitacion")
    Invitacion buscarInvitacionPorId(@Param("idInvitacion") Long idInvitacion);

    @Query("SELECT i FROM Invitacion i WHERE i.destinatario.id  = :idPerfil AND i.aceptada = false AND i.fechaDeBaja IS NULL")
    List<Invitacion> invitacionesPendiente(@Param("idPerfil") Long idPerfil);

    @Query("SELECT i FROM Invitacion i WHERE ((i.remitente.id = :idRemitente AND i.destinatario.id = :idDestinatario) OR (i.remitente.id = :idDestinatario AND i.destinatario.id = :idRemitente))AND i.fechaDeBaja IS NULL")
    Invitacion buscarInvitacionEntreDosPerfiles(@Param("idRemitente") Long idRemitente, @Param("idDestinatario") Long idDestinatario);
}
