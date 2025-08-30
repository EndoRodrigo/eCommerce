package com.endorodrigo.eCommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Controlador para manejar errores HTTP (404, 500, etc.)
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        logger.error("Error HTTP: {} - Mensaje: {} - Excepción: {}", status, message, exception);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            switch (statusCode) {
                case 404:
                    model.addAttribute("errorMessage", "La página que buscas no existe.");
                    model.addAttribute("errorCode", "404");
                    model.addAttribute("timestamp", new Date());
                    return "error/404";
                    
                case 403:
                    model.addAttribute("errorMessage", "No tienes permisos para acceder a este recurso.");
                    model.addAttribute("errorCode", "403");
                    model.addAttribute("timestamp", new Date());
                    return "error/generic-error";
                    
                case 500:
                    model.addAttribute("errorMessage", "Error interno del servidor. Por favor, inténtelo de nuevo.");
                    model.addAttribute("errorCode", "500");
                    model.addAttribute("timestamp", new Date());
                    return "error/generic-error";
                    
                default:
                    model.addAttribute("errorMessage", "Ha ocurrido un error inesperado.");
                    model.addAttribute("errorCode", statusCode);
                    model.addAttribute("timestamp", new Date());
                    return "error/generic-error";
            }
        }
        
        // Error genérico si no se puede determinar el código de estado
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado.");
        model.addAttribute("errorCode", "Error");
        model.addAttribute("timestamp", new Date());
        return "error/generic-error";
    }
}
