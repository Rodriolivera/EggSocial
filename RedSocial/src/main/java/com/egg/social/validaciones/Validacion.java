package com.egg.social.validaciones;

import com.egg.social.excepciones.ExcepcionSpring;

public class Validacion {

    public static void validarUsuario(String correo, String clave, String clave2) throws ExcepcionSpring {
        if (correo == null || correo.isEmpty()) {
            throw new ExcepcionSpring("El correo electrónico no puede ser nulo");
        }

        if (clave == null || correo.isEmpty()) {
            throw new ExcepcionSpring("La contraseña no puede ser nula");
        }

        if (clave2 == null || clave2.isEmpty()) {
            throw new ExcepcionSpring("La repetición de la contraseña no puede ser nula");
        }

        if (!clave.equals(clave2)) {
            throw new ExcepcionSpring("Ambas contraseñas deben ser iguales");
        }
    }

    public static void validarUsuario(Long id, String clave) throws ExcepcionSpring {
        if (id == null) {
            throw new ExcepcionSpring("El ID del usuario no puede ser nulo");
        }

        if (clave == null || clave.isEmpty()) {
            throw new ExcepcionSpring("La nueva contraseña no puede ser nula");
        }
    }

    public static void validarPerfil(Long idUsuario, String nombre, String apellido, String residencia) throws ExcepcionSpring {
        if (idUsuario == null) {
            throw new ExcepcionSpring("El ID no puede ser nulo");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new ExcepcionSpring("El nombre no puede ser nulo");
        }

        if (nombre.matches("^-?[0-9]+$")) {
            throw new ExcepcionSpring("Sólo se admiten caracteres en el nombre");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ExcepcionSpring("El apellido no puede ser nulo");
        }

        if (apellido.matches("^-?[0-9]+$")) {
            throw new ExcepcionSpring("Sólo se admiten caracteres en el apellido");
        }

        if (residencia == null || residencia.isEmpty()) {
            throw new ExcepcionSpring("La residencia no puede ser nula");
        }

        if (residencia.matches("^-?[0-9]+$")) {
            throw new ExcepcionSpring("Sólo se admiten caracteres en la residencia");
        }
    }
}
