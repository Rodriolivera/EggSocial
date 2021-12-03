package com.egg.social.servicios;

import com.egg.social.entidades.Perfil;
import com.egg.social.repositorios.PublicacionRepositorio;
import com.egg.social.entidades.Publicacion;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.repositorios.InvitacionRepositorio;
import com.egg.social.repositorios.PerfilRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PublicacionServicio {

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private PerfilRepositorio perfilRepositorio;

    @Autowired
    private InvitacionRepositorio invitacionRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void crearPublicacion(Long idPerfil, String descripcion, MultipartFile foto) throws ExcepcionSpring {
        try {
            Perfil perfil = perfilRepositorio.buscarPerfilPorId(idPerfil);

            if (perfil != null) {
                if (descripcion.trim().equals("") && foto.isEmpty()) {
                    throw new ExcepcionSpring("No se puede crear una Publicación sin contenido");
                }

                Publicacion publicacion = new Publicacion();

                publicacion.setPerfil(perfil);
                publicacion.setDescripcion(descripcion.trim());
                publicacion.setFechaDePublicacion(new Date());

                if (!foto.isEmpty()) {
                    if (foto.getContentType().substring(0,6).equalsIgnoreCase("image/")) {
                        publicacion.setFoto(fotoServicio.guardarFoto(foto));
                    } else {
                        throw new ExcepcionSpring("Sólo puede subir archivos del tipo imágen.");
                    }
                }

                publicacionRepositorio.save(publicacion);
            } else {
                throw new ExcepcionSpring("No existe un usuario con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al crear publicación");
        }
    }

    @Transactional
    public void modificarPublicacion(Long idPublicacion, String descripcion, MultipartFile foto) throws ExcepcionSpring {
        try {
            Publicacion publicacion = publicacionRepositorio.buscarPublicacionPorId(idPublicacion);

            if (publicacion != null) {
                publicacion.setDescripcion(descripcion);

                if (!foto.isEmpty()) {
                    publicacion.setFoto(fotoServicio.guardarFoto(foto));
                }

                publicacionRepositorio.save(publicacion);
            } else {
                throw new ExcepcionSpring("No se ha encontrado ninguna publicación para editar");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al modificar publicación");
        }
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarPublicaciones(Long idUsuario) throws ExcepcionSpring {
        try {
            Perfil perfil = perfilRepositorio.buscarPerfilPorIdDeUsuario(idUsuario);

            if (perfil != null) {
                List<Long> listaDeAmigos = invitacionRepositorio.listaDestinatario(perfil.getId());
                listaDeAmigos.addAll(invitacionRepositorio.listaRemitente(perfil.getId()));
                listaDeAmigos.add(perfil.getId());

                List<Publicacion> publicaciones = publicacionRepositorio.findByPerfil_IdInAndFechaDeBajaIsNullOrderByFechaDePublicacionDesc(listaDeAmigos);

                List<Publicacion> publicacionesRetorno = new ArrayList<>();

                for (Publicacion publicacion : publicaciones) {
                    publicacion.setComentarios(publicacion.getComentarios().stream()
                            .filter(c -> c.getFechaDeBaja() == null).collect(Collectors.toList()));
                    publicacionesRetorno.add(publicacion);
                }

                return publicacionesRetorno;
            } else {
                throw new ExcepcionSpring("No existen un usuario con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al buscar publicaciones");
        }
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarPublicacionesPorPerfil(Perfil perfil) {
        List<Publicacion> publicaciones = publicacionRepositorio.publicacionesPorPerfil(perfil.getId());

        return publicaciones;
    }

    @Transactional(readOnly = true)
    public Publicacion buscarPublicacionPorId(Long idPublicacion) {
        return publicacionRepositorio.findById(idPublicacion).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarTodas() {
        return publicacionRepositorio.findAll();
    }

    @Transactional
    public void eliminarPublicacion(Long idPublicacion) throws ExcepcionSpring {
        try {
            Publicacion publicacion = publicacionRepositorio.buscarPublicacionPorId(idPublicacion);

            if (publicacion != null) {
                publicacion.setFechaDeBaja(new Date());

                publicacionRepositorio.save(publicacion);
            } else {
                throw new ExcepcionSpring("No existe una publicación con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al eliminar publicación");
        }
    }
}
