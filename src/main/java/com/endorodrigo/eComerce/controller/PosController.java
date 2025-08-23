package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.*;
import com.endorodrigo.eComerce.service.CustomerService;
import com.endorodrigo.eComerce.service.PosService;
import com.endorodrigo.eComerce.service.ItemService;
import com.endorodrigo.eComerce.service.WebClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


/**
 * Controlador para la gestión del punto de venta (POS).
 * Permite agregar productos y clientes al carrito y procesar ventas.
 */
@Controller
@RequestMapping("/pos")
@SessionAttributes("data")
public class PosController {

    private static final Logger logger = LoggerFactory.getLogger(PosController.class);

    @Autowired
    private ItemService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PosService posService;

    @Autowired
    private WebClientService webClientService;

    private final Cart cart = new Cart();

    @GetMapping
    public String pos(@RequestParam(defaultValue = "1") int step, Model model) {
        cart.setReference_code(posService.generateIdCard());
        model.addAttribute("cart", cart);
        model.addAttribute("data", new Data());
        model.addAttribute("currentStep", step);
        // Muestra la vista POS
        return "pos";
    }

    @PostMapping(params = "action=saveClient")
    public String saveCliente(@ModelAttribute("data") Data data, Model model) {
        Customer customer = customerService.findIdIntification(data.getClient());
        logger.info("Information customer -> {}", customer);
        if (customer == null) {
            model.addAttribute("msg2", "Customer not found");
            return "pos";
        }
        cart.setCustomer(customer);
        return "redirect:/pos?step=2";
    }

    @PostMapping(params = "action=addProduct")
    public String addProduct(@ModelAttribute("data") Data data, Model model) {
        Item product = productService.findId(Long.valueOf(data.getCode()));
        logger.info("Information product = {}", product);
        if (product == null) {
            model.addAttribute("msg1", "Product not found");
            return "pos";
        }
        cart.addProduct(product);

        return "redirect:/pos?step=2";
    }


    @PostMapping(params = "action=nextToPayment")
    public String nextToPayment() {
        return "redirect:/pos?step=3";
    }

    @PostMapping(params = "action=nextAddPayment")
    public String nextToPayment(@ModelAttribute("data") Data data, Model model) {
        cart.setPayment_method_code(data.getPaymentMethod());
        logger.info("Information pago = {}", data.getPaymentMethod());
        if (data.getPaymentMethod() == null) {
            model.addAttribute("msg1", "Payment not found");
            return "pos";
        }
        return "redirect:/pos?step=3";
    }

    @PostMapping("/generate")
    public String submit(SessionStatus status) {
        logger.info("Information cart (antes de enviar) = {}", cart);

        // Guardar venta local
        posService.insert(cart);

        // Enviar a Factus
        webClientService.createPost(cart);

        // Limpiar sesión después
        status.setComplete();
        cart.clear();
        return "redirect:/pos";
    }

    @PostMapping("/pos/clear")
    public void clearCart() {
        cart.clear();
    }
}
