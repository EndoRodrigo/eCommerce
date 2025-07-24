package com.endorodrigo.eComerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.security.Principal;

/**
 * Controlador para la página principal del sistema.
 * Muestra la vista de inicio para usuarios autenticados.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Principal principal, Model model) {
        logger.info("Usuario Logueado -> {}", principal.getName());
        // Muestra la página principal
        return "index";
    }
}
