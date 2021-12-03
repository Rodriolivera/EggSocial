package com.egg.social.repositorios;

import com.egg.social.entidades.Perfil;
import com.egg.social.entidades.Publicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByPerfil(Perfil perfil);

    List<Publicacion> findByFechaDeBajaIsNull();

    List<Publicacion> findByPerfil_IdIn(List<Long> amigos);

    @Query("SELECT p FROM Publicacion p WHERE p.perfil.id = :idPerfil AND p.fechaDeBaja IS NULL ORDER BY p.fechaDePublicacion DESC")
    List<Publicacion> publicacionesPorPerfil(@Param("idPerfil") Long idPerfil);

    @Query("SELECT p FROM Publicacion p WHERE p.id = :idPublicacion AND p.fechaDeBaja IS NULL")
    Publicacion buscarPublicacionPorId(@Param("idPublicacion") Long idPublicacion);

    @Query("SELECT p FROM Publicacion p WHERE p.perfil.id = :idPerfil AND p.fechaDeBaja IS NULL ORDER BY p.fechaDePublicacion")
    List<Publicacion> publicacionesDeAmigos(@Param("idPerfil") Long idPerfil);

    List<Publicacion> findByPerfil_IdInAndFechaDeBajaIsNullOrderByFechaDePublicacionDesc(List<Long> amigos);
}
