package com.endorodrigo.eCommerce.controller;

import com.endorodrigo.eCommerce.model.Role;
import com.endorodrigo.eCommerce.model.UserRegistrationDTO;
import com.endorodrigo.eCommerce.service.AuthService;
import com.endorodrigo.eCommerce.service.RolService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador para autenticación y registro de usuarios.
 * Gestiona las vistas de login y registro.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final RolService rolService;

    public AuthController(AuthService authService, RolService rolService) {
        this.authService = authService;
        this.rolService = rolService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        // Muestra el formulario de login
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        // Muestra el formulario de registro
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO user,
                               Errors errors,
                               Model model) {
        logger.info("Datos de registro recibidos: {}", user);
        
        if (errors.hasErrors()) {
            logger.warn("Errores de validación en el formulario: {}", errors.getAllErrors());
            // Si hay errores de validación, vuelve a mostrar el formulario
            return "register";
        }
        
        // Validar que las contraseñas coincidan
        if (!user.isPasswordMatching()) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }
        
        try {
            // Obtener el rol por ID
            Role role = rolService.findById(user.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol no válido"));
            
            // Registrar el usuario
            authService.registerUser(user, role);
            
            logger.info("Usuario registrado exitosamente: {}", user.getEmail());
            return "redirect:/login?registered=true";
            
        } catch (IllegalArgumentException ex) {
            logger.warn("Error de validación en el registro: {}", ex.getMessage());
            model.addAttribute("error", ex.getMessage());
            return "register";
        } catch (RuntimeException ex) {
            logger.error("Error durante el registro del usuario: {}", ex.getMessage());
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
