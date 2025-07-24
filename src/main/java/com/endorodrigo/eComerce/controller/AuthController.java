package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.User;
import com.endorodrigo.eComerce.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controlador para autenticación y registro de usuarios.
 * Gestiona las vistas de login y registro.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String showLoginForm() {
        // Muestra el formulario de login
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        // Muestra el formulario de registro
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               Errors errors,
                               Model model) {
        logger.info("Datos recibidos: {}", user);
        if (errors.hasErrors()) {
            // Si hay errores de validación, vuelve a mostrar el formulario
            return "register";
        }
        try {
            logger.info("Guardando usuario: {}", user.getUsername());
            authService.registerUser(user);
            return "redirect:/";
        } catch (RuntimeException ex) {
            // Muestra el error en el formulario
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
