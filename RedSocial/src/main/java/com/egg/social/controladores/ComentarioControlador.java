package com.egg.social.controladores;

import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.servicios.ComentarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;

    @PostMapping("/crear-comentario")
    public RedirectView guardar(@RequestParam("idPerfil") Long idPerfil, @RequestParam("idPublicacion") Long idPublicacion, @RequestParam String descripcion) throws ExcepcionSpring {

        comentarioServicio.crearComentario(idPerfil, idPublicacion, descripcion);

        return new RedirectView("/");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarComentario(@PathVariable Long id) throws ExcepcionSpring {

        comentarioServicio.eliminarComentario(id);

        return new RedirectView("/");
    }

}
