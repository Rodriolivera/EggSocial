package com.egg.social.repositorios;

import com.egg.social.entidades.Perfil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepositorio extends JpaRepository<Perfil, Long> {

    @Query("SELECT p FROM Perfil p WHERE p.id = :idPerfil AND p.fechaDeBaja IS NULL")
    Perfil buscarPerfilPorId(@Param("idPerfil") Long idPerfil);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.id = :idUsuario")
    Perfil buscarPerfilPorIdDeUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM Perfil p WHERE p.apellido = :apellido AND p.nombre = :nombre")
    List<Perfil> buscarPorNombreYApellido(@Param("apellido") String apellido, @Param("nombre") String nombre);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.fechaDeBaja IS NULL")
    List<Perfil> buscarPerfiles();

    @Query("SELECT p FROM Perfil p ORDER BY p.usuario.rol.nombre")
    List<Perfil> buscarTodos();

    @Query("SELECT p FROM Perfil p WHERE (p.nombre LIKE CONCAT('%', :nombre, '%') AND p.apellido LIKE CONCAT('%', :apellido, '%')) AND p.fechaDeBaja IS NULL")
    List<Perfil> buscarPerfilesPorNombreYApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

    @Query("SELECT p FROM Perfil p WHERE (p.nombre LIKE CONCAT('%', :dato, '%') OR p.apellido LIKE CONCAT('%', :dato, '%')) AND p.fechaDeBaja IS NULL")
    List<Perfil> buscarPerfilesPorNombreOApellido(@Param("dato") String dato);
}
