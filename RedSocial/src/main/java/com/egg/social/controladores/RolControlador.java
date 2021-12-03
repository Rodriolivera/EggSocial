package com.egg.social.controladores;

import com.egg.social.entidades.Invitacion;
import com.egg.social.entidades.Perfil;
import com.egg.social.entidades.Publicacion;
import com.egg.social.entidades.Rol;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.servicios.InvitacionServicio;
import com.egg.social.servicios.PerfilServicio;
import com.egg.social.servicios.PublicacionServicio;
import com.egg.social.servicios.RolServicio;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RolControlador {

    @Autowired
    private RolServicio rolServicio;

    @Autowired
    private PerfilServicio perfilServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private InvitacionServicio invitacionServicio;

    @GetMapping
    public ModelAndView mostrarRoles(HttpServletRequest request, HttpSession sesion) throws ExcepcionSpring {
        ModelAndView mav = new ModelAndView("lista-roles");

        Perfil perfil = perfilServicio.buscarPerfilPorIdUsuario((Long) sesion.getAttribute("idUsuario"));
        List<Perfil> perfiles = perfilServicio.mostrarTodos();

        List<Publicacion> publicaciones = publicacionServicio.buscarPublicacionesPorPerfil(perfil);

        List<Invitacion> invitacionesPendientes = invitacionServicio.invitacionesRecibidasPendientes(perfil);

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("mensaje", flashMap.get("creado"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("publicacion", new Publicacion());
        mav.addObject("publicaciones", publicaciones);

        mav.addObject("perfil", perfil);
        mav.addObject("perfilFeed", perfil);
        mav.addObject("perfiles", perfilServicio.listaDeCuatro(perfiles, perfil.getId()));
        mav.addObject("amigos", perfilServicio.obtenerAmigos((Long) sesion.getAttribute("idUsuario")));
        mav.addObject("cantidadInvitaciones", invitacionesPendientes.size());

        mav.addObject("roles", rolServicio.buscarRoles());

        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearRol(HttpSession sesion) throws ExcepcionSpring {
        ModelAndView mav = new ModelAndView("rol-formulario");

        Perfil perfil = perfilServicio.buscarPerfilPorIdUsuario((Long) sesion.getAttribute("idUsuario"));
        List<Invitacion> invitacionesPendientes = invitacionServicio.invitacionesRecibidasPendientes(perfil);

        mav.addObject("rol", new Rol());
        mav.addObject("perfil", perfil);
        mav.addObject("perfilFeed", perfil);
        mav.addObject("cantidadInvitaciones", invitacionesPendientes.size());

        mav.addObject("action", "guardar-rol");

        return mav;
    }

    @PostMapping("/guardar-rol")
    public RedirectView guardarRol(@RequestParam String nombre, RedirectAttributes redirectAttributes) {
        try {
            rolServicio.crearRol(nombre);
            redirectAttributes.addFlashAttribute("creado", "Rol creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/roles");
    }
}
