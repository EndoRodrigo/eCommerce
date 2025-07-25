package com.endorodrigo.eComerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.endorodrigo.eComerce.service.DashboardService;
import java.security.Principal;

/**
 * Controlador para la página principal del sistema.
 * Muestra la vista de inicio para usuarios autenticados y dashboard.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Principal principal, Model model) {
        logger.info("Usuario Logueado -> {}", principal.getName());
        // Datos del dashboard
        model.addAttribute("totalVentas", dashboardService.getTotalVentas());
        model.addAttribute("totalProductos", dashboardService.getTotalProductos());
        model.addAttribute("totalClientes", dashboardService.getTotalClientes());
        model.addAttribute("totalUsuarios", dashboardService.getTotalUsuarios());
        model.addAttribute("totalCarritos", dashboardService.getTotalCarritos());
        // Muestra la página principal
        return "index";
    }
}
