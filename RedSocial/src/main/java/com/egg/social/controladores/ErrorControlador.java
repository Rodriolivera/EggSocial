package com.egg.social.controladores;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView paginaDeError(HttpServletRequest respuestaHTTP, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("error");
        String mensajeDeError = "";

        int codigoDeError = response.getStatus();

        switch (codigoDeError) {
            case 400: {
                mensajeDeError = "El recurso solicitado no existe.";
                break;
            }
            case 401: {
                mensajeDeError = "No se encuentra autorizado.";
                break;
            }
            case 403: {
                mensajeDeError = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 404: {
                mensajeDeError = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                mensajeDeError = "Ocurrió un error interno.";
                break;
            }
            default: {
                mensajeDeError = "Se produjo un Error Inesperado.";
                break;
            }
        }

        mav.addObject("request", respuestaHTTP);
        mav.addObject("response", response);
        mav.addObject("codigo", codigoDeError);
        mav.addObject("mensaje", mensajeDeError);

        return mav;
    }
}
