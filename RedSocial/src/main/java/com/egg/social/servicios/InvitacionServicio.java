package com.egg.social.servicios;

import com.egg.social.entidades.Invitacion;
import com.egg.social.entidades.Perfil;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.repositorios.InvitacionRepositorio;
import com.egg.social.repositorios.PerfilRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitacionServicio {

    @Autowired
    private InvitacionRepositorio invitacionRepositorio;

    @Autowired
    private PerfilRepositorio perfilRepositorio;

    @Transactional
    public Invitacion crearInvitacion(Perfil remitente, Perfil destinatario) throws ExcepcionSpring {
        try {
            if (remitente != null && destinatario != null) {
                if (comprobarInvitacion(remitente, destinatario)) {
                    Invitacion invitacion = new Invitacion();
                    invitacion.setRemitente(remitente);
                    invitacion.setDestinatario(destinatario);
                    invitacion.setAceptada(false);

                    remitente.getInvitacionesEnviadas().add(invitacion);
                    destinatario.getInvitacionesRecibidas().add(invitacion);

                    perfilRepositorio.save(remitente);
                    perfilRepositorio.save(destinatario);

                    invitacionRepositorio.save(invitacion);

                    return invitacion;
                } else {
                    throw new ExcepcionSpring("La invitación ya existe");
                }
            } else {
                throw new ExcepcionSpring("Es necesario que exista tanto un remitente como un destinatario");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al crear invitación");
        }
    }

    @Transactional(readOnly = true)
    public boolean comprobarInvitacion(Perfil remitente, Perfil destinatario) {
        Invitacion invitacion = invitacionRepositorio.buscarInvitacionEntreDosPerfiles(remitente.getId(), destinatario.getId());

        if (invitacion != null) {
            return false;
        }

        return true;
    }

    @Transactional(readOnly = true)
    public boolean sonAmigos(Perfil remitente, Perfil destinatario) {
        if (!comprobarInvitacion(remitente, destinatario)) {
            Invitacion invitacion = invitacionRepositorio.buscarInvitacionEntreDosPerfiles(remitente.getId(), destinatario.getId());
            if (invitacion.getAceptada() == true) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void aceptarInvitacion(Long idInvitacion) throws ExcepcionSpring {
        try {
            Invitacion invitacion = invitacionRepositorio.buscarInvitacionPorId(idInvitacion);

            if (invitacion != null) {
                invitacion.setAceptada(true);

                invitacionRepositorio.save(invitacion);
            } else {
                throw new ExcepcionSpring("No existe una invitación con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al aceptar invitación");
        }
    }

    @Transactional
    public void rechazarInvitacion(Long idInvitacion) throws ExcepcionSpring {
        try {
            Invitacion invitacion = invitacionRepositorio.buscarInvitacionPorId(idInvitacion);

            if (invitacion != null) {
                invitacion.setFechaDeBaja(new Date());

                invitacionRepositorio.save(invitacion);
            } else {
                throw new ExcepcionSpring("No existe una invitación con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al rechazar invitación");
        }
    }

    @Transactional(readOnly = true)
    public List<Invitacion> invitacionesRecibidasPendientes(Perfil perfil) throws ExcepcionSpring {
        try {
            if (perfil != null) {

                return invitacionRepositorio.invitacionesPendiente(perfil.getId());
            } else {
                throw new ExcepcionSpring("No existe un usuario con el ID indicado en invitaciones");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al buscar invitaciones recibidas");
        }
    }

    @Transactional(readOnly = true)
    public List<Invitacion> invitacionesEnviadasPendientes(Long idUsuario) throws ExcepcionSpring {
        try {
            Perfil perfil = perfilRepositorio.buscarPerfilPorIdDeUsuario(idUsuario);

            if (perfil != null) {
                List<Invitacion> enviadasPendientes = new ArrayList<>();

                for (Invitacion invitacion : perfil.getInvitacionesEnviadas()) {
                    if (invitacion.getAceptada() == false) {
                        enviadasPendientes.add(invitacion);
                    }
                }

                return enviadasPendientes;
            } else {
                throw new ExcepcionSpring("No existe un usuario con el ID indicado");
            }
        } catch (ExcepcionSpring e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al buscar invitaciones enviadas");
        }
    }

    @Transactional(readOnly = true)
    public Invitacion invitacionesEntreDosPerfiles(Perfil remitente, Perfil destinatario) throws ExcepcionSpring {
        Invitacion invitacion = invitacionRepositorio.buscarInvitacionEntreDosPerfiles(remitente.getId(), destinatario.getId());

        return invitacion;
    }
}
