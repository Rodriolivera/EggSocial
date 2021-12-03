package com.egg.social.controladores;

import com.egg.social.entidades.Invitacion;
import com.egg.social.entidades.Perfil;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.servicios.InvitacionServicio;
import com.egg.social.servicios.PerfilServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/invitaciones")
public class InvitacionControlador {

    @Autowired
    private PerfilServicio perfilServicio;
    @Autowired
    private InvitacionServicio invitacionServicio;

    @GetMapping("/lista")
    public ModelAndView mostrarInvitacionPendiente(HttpSession session) throws ExcepcionSpring {
        ModelAndView mav = new ModelAndView("lista-invitaciones");

        Perfil perfil = perfilServicio.buscarPerfilPorIdUsuario((Long) session.getAttribute("idUsuario"));

        List<Invitacion> invitacionesPendientes = invitacionServicio.invitacionesRecibidasPendientes(perfil);

        mav.addObject("lista", invitacionesPendientes);
        mav.addObject("perfil", perfil);
        mav.addObject("perfilFeed", perfil);
        mav.addObject("cantidadInvitaciones", invitacionesPendientes.size());

        return mav;
    }

    @PostMapping("/aceptar")
    public RedirectView aceptarInvitacion(@RequestParam("id") Long id) throws ExcepcionSpring {
        invitacionServicio.aceptarInvitacion(id);

        return new RedirectView("/invitaciones/lista");
    }

    @PostMapping("/rechazar")
    public RedirectView rechazarInvitacion(@RequestParam("id") Long id) throws ExcepcionSpring {
        invitacionServicio.rechazarInvitacion(id);

        return new RedirectView("/invitaciones/lista");
    }

    @PostMapping("/eliminar")
    public RedirectView eliminarAmistad(@RequestParam("id") Long id) throws ExcepcionSpring {
        invitacionServicio.rechazarInvitacion(id);

        return new RedirectView("/invitaciones/lista");
    }

    @PostMapping("/invitar")
    public RedirectView invitarAmigo(@RequestParam("idRemitente") Long idPerfil, @RequestParam("idDestinatario") Long idPerfil2) throws ExcepcionSpring {
        Perfil perfil = perfilServicio.obtenerPerfil(idPerfil);
        Perfil perfil2 = perfilServicio.obtenerPerfil(idPerfil2);
        invitacionServicio.crearInvitacion(perfil, perfil2);

        return new RedirectView("/perfil");
    }
}
