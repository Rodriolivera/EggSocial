package com.egg.social.repositorios;

import com.egg.social.entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {

    @Query("SELECT c FROM Comentario c WHERE c.id = :idComentario and c.fechaDeBaja is NULL")
    Comentario buscarComentarioPorId(@Param("idComentario") Long idComentario);
}
