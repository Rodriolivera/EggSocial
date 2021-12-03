package com.egg.social.repositorios;

import com.egg.social.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Usuario buscarUsuarioPorCorreo(@Param("correo") String correo);

    @Query("SELECT u FROM Usuario u WHERE u.fechaDeBaja IS NULL")
    List<Usuario> buscarUsuarios();

    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Usuario buscarUsuarioPorId(@Param("id") Long id);
}
