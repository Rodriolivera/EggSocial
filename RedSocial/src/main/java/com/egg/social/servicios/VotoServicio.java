package com.egg.social.servicios;

import com.egg.social.entidades.Perfil;
import com.egg.social.entidades.Publicacion;
import com.egg.social.entidades.Voto;
import com.egg.social.excepciones.ExcepcionSpring;
import com.egg.social.repositorios.PerfilRepositorio;
import com.egg.social.repositorios.VotoRepositorio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotoServicio {

    @Autowired
    private VotoRepositorio votoRepositorio;

    @Autowired
    private PerfilRepositorio perfilRepositorio;

    @Transactional
    public void votarPerfil(Long idUsuario, Publicacion publicacion) throws ExcepcionSpring {
        try {
            Perfil perfil = perfilRepositorio.buscarPerfilPorIdDeUsuario(idUsuario);
            Voto voto = votoRepositorio.buscarVoto(perfil.getId(), publicacion.getId());

            if (voto == null) {
                voto = new Voto();

                voto.setPerfil(perfil);
                voto.setPublicacion(publicacion);
            } else if (voto.getFechaDeBaja() == null) {
                voto.setFechaDeBaja(new Date());
            } else {
                voto.setFechaDeBaja(null);
            }

            votoRepositorio.save(voto);
        } catch (Exception e) {
            throw new ExcepcionSpring("Error al votar");
        }
    }
}
