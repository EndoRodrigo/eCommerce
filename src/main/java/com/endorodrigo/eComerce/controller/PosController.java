package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.*;
import com.endorodrigo.eComerce.service.CustomerService;
import com.endorodrigo.eComerce.service.PosService;
import com.endorodrigo.eComerce.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Controlador para la gestión del punto de venta (POS).
 * Permite agregar productos y clientes al carrito y procesar ventas.
 */
@Controller
public class PosController {

    private static final Logger logger = LoggerFactory.getLogger(PosController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PosService posService;

    private final Cart cart = new Cart();

    @RequestMapping(value = "/pos", method = RequestMethod.GET)
    public String pos(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("data", new Data());
        // Muestra la vista POS
        return "pos";
    }

    @RequestMapping(value = "/pos/add", method = RequestMethod.POST)
    public String addProduct(@Valid @ModelAttribute("data") Data cartForm, Errors errors, Model model ) {
        if (errors.hasErrors()) {
            model.addAttribute("cart", cart);
            return "pos";
        }
        logger.info("addProduct for id -> {}", cartForm.getCode());
        Items product = productService.findId(cartForm.getCode());
        logger.info("addCliente for id -> {}", cartForm.getClient());
        Customer customer = customerService.findId(cartForm.getClient());
        logger.info("product = {}", product);
        if (product == null && customer == null) {
            model.addAttribute("cart", cart);
            model.addAttribute("msg1", "Product not found");
            model.addAttribute("msg2", "Customer not found");
            return "pos";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        Long timestamp = Long.valueOf(LocalDateTime.now().format(formatter));
        cart.setIdCar(timestamp);
        cart.addProduct(product);
        cart.setCustomer(customer);
        // Integración de medios de pago
        String paymentMethod = cartForm.getPaymentMethod() != null ? cartForm.getPaymentMethod() : "Efectivo";
        cart.setPayment(new Payment("Pago de compra", cart.getTotal(), paymentMethod, "Pendiente"));
        logger.info("list cart = " + cart);
        return "redirect:/pos";

    }

    @RequestMapping(value = "/pos/clear", method =  RequestMethod.GET)
    public String clearCart(Model model) {
        logger.info("clearCart for id. "+ cart.toString());
        cart.clear();
        logger.info("list carts = " + cart);
        return "redirect:/pos";
    }

    @RequestMapping(value = "/pos/cash", method =  RequestMethod.GET)
    public String Order(){
        logger.info("Order for id. "+ cart);
        posService.insert(cart);
        cart.clear();
        return "redirect:/pos";
    }

    @RequestMapping(value = "/pos/update", method = RequestMethod.POST)
    public String updateCart(@ModelAttribute("cart") Cart cartForm, Model model) {
        posService.update(cartForm);
        model.addAttribute("cart", cartForm);
        return "redirect:/pos";
    }
}
