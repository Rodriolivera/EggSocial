package com.egg.social.repositorios;

import com.egg.social.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Long> {

    @Query("SELECT r FROM Rol r WHERE r.nombre = 'USUARIO'")
    Rol buscarRolUsuario();

    @Query("SELECT r FROM Rol r WHERE r.nombre = 'ADMIN'")
    Rol buscarRolAdministrador();

    @Query("SELECT r FROM Rol r WHERE r.nombre = :nombre")
    Rol findByNombre(@Param("nombre") String nombre);
}
