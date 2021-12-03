package com.egg.social.controladores;

import com.egg.social.entidades.Invitacion;
import com.egg.social.entidades.Perfil;
import com.egg.social.servicios.PublicacionServicio;
import com.egg.social.entidades.Publicacion;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.servicios.InvitacionServicio;
import com.egg.social.servicios.PerfilServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionControlador {

    @Autowired
    private PublicacionServicio publicacionServicio;
    @Autowired
    private PerfilServicio perfilServicio;

    @Autowired
    private InvitacionServicio invitacionServicio;

    @GetMapping("/nueva")
    public ModelAndView mostrarFormulario() {
        return new ModelAndView("formulario-publicacion");
    }

    @PostMapping("/guardar-publicacion")
    public RedirectView guardar(@RequestParam Long id, @RequestParam(required = false) String descripcion, @RequestParam(required = false) MultipartFile foto, RedirectAttributes redirectAttributes) {
        try {
            publicacionServicio.crearPublicacion(id, descripcion, foto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/perfil");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView modificar(@PathVariable Long id, HttpSession session) {
        ModelAndView mav = new ModelAndView("publicacion-editar");

        try {
            Publicacion publicacion = publicacionServicio.buscarPublicacionPorId(id);
            Long idUsuario = publicacion.getPerfil().getUsuario().getId();
            
            if (!(session.getAttribute("idUsuario").equals(idUsuario))) {
                return new ModelAndView("/perfil");
            }
            
            Perfil perfil = perfilServicio.buscarPerfilPorIdUsuario((Long) session.getAttribute("idUsuario"));

            List<Invitacion> invitacionesPendientes = invitacionServicio.invitacionesRecibidasPendientes(perfil);

            mav.addObject("perfil", perfil);
            mav.addObject("perfilFeed", perfil);
            mav.addObject("perfiles", perfilServicio.obtenerAmigos((Long) session.getAttribute("idUsuario")));
            mav.addObject("cantidadInvitaciones", invitacionesPendientes.size());
            mav.addObject("publicacion", publicacion);
            mav.addObject("action", "guardar-modificado");
            return mav;
        } catch (ExcepcionSpring e) {
            mav = new ModelAndView("/");
            mav.addObject("error", e.getMessage());
        }

        return new ModelAndView("/perfil");
    }

    @PostMapping("/guardar-modificado")
    public RedirectView guardarModificado(@RequestParam("id") Long id, @RequestParam String descripcion, @RequestParam(required = false) MultipartFile foto) {
        try {
            publicacionServicio.modificarPublicacion(id, descripcion, foto);
        } catch (ExcepcionSpring e) {
            System.err.println(e.getMessage());
        }

        return new RedirectView("/perfil");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarPublicacion(@PathVariable Long id) throws ExcepcionSpring {
        publicacionServicio.eliminarPublicacion(id);

        return new RedirectView("/perfil");
    }
}
