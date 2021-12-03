package com.egg.social.servicios;

import com.egg.social.excepciones.ExcepcionSpring;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Transactional
    public String guardarFoto(MultipartFile archivo) throws ExcepcionSpring {
        String nombreDelArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
        Path rutaDelArchivo = Paths.get("src//main//resources//static//foto").resolve(nombreDelArchivo).toAbsolutePath();

        try {
            Files.copy(archivo.getInputStream(), rutaDelArchivo);
        } catch (IOException e) {
            throw new ExcepcionSpring("Error al subir la foto del perfil en la base de datos");
        }

        return nombreDelArchivo;
    }
}
