package com.egg.social.servicios;

import com.egg.social.entidades.Usuario;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.repositorios.RolRepositorio;
import com.egg.social.repositorios.UsuarioRepositorio;
import com.egg.social.validaciones.Validacion;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RolServicio rolServicio;

    @Transactional
    public Usuario crearUsuario(String correo, String clave, String clave2) throws ExcepcionSpring {
        try {
            Validacion.validarUsuario(correo, clave, clave2);

            if (usuarioRepositorio.buscarUsuarioPorCorreo(correo) == null) {
                Usuario usuario = new Usuario();

                usuario.setCorreo(correo);
                usuario.setClave(encoder.encode(clave));

                if (usuarioRepositorio.findAll().isEmpty()) {
                    rolServicio.crearRol("ADMIN");
                    usuario.setRol(rolRepositorio.buscarRolAdministrador());
                } else {
                    usuario.setRol(rolRepositorio.buscarRolUsuario());
                }

                return usuarioRepositorio.save(usuario);
            } else {
                throw new ExcepcionSpring("Ya existe un usuario con el correo ingresado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al registrar usuario");
        }
    }

    public void modificarUsuario(Long idUsuario, String clave) throws ExcepcionSpring {
        try {
            Validacion.validarUsuario(idUsuario, clave);

            Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(idUsuario);

            if (usuario != null) {
                usuario.setClave(encoder.encode(clave));

                usuarioRepositorio.save(usuario);
            } else {
                throw new ExcepcionSpring("No existe un usuario con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al modificar usuario");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios() throws ExcepcionSpring {
        try {
            List<Usuario> usuarios = usuarioRepositorio.buscarUsuarios();

            if (!usuarios.isEmpty()) {
                return usuarios;
            } else {
                throw new ExcepcionSpring("No existen usuarios registrados en la plataforma");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al buscar usuarios");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarUsuarioPorCorreo(correo);

        if (usuario == null) {
            throw new UsernameNotFoundException("No existe un usuario registrado con el correo electrónico ingresado");
        }

        if (usuario.getFechaDeBaja() != null) {
            throw new UsernameNotFoundException("No existe un usuario dado de baja");
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpSession sesion = attr.getRequest().getSession(true);

        sesion.setAttribute("idUsuario", usuario.getId());
        sesion.setAttribute("correo", usuario.getCorreo());

        GrantedAuthority rol = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(rol));
    }
}
