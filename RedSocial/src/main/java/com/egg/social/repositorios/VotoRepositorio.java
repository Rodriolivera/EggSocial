package com.egg.social.repositorios;

import com.egg.social.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, Long> {

    @Query("SELECT v FROM Voto v WHERE v.perfil.id = :idPerfil AND v.publicacion.id = :idPublicacion")
    Voto buscarVoto(@Param("idPerfil") Long idPerfil, @Param("idPublicacion") Long IdPublicacion);

    @Query("SELECT i.id FROM Invitacion i WHERE (i.remitente.id = :idRemitente AND i.destinatario.id = :idDestinatario"
            + " OR i.remitente.id = :idDestinatario AND i.destinatario.id = :idRemitente) AND i.fechaDeBaja IS NULL")
    Long buscarIdInvitacion(@Param("idRemitente") Long idRemitente, @Param("idDestinatario") Long idDestinatario);
}
