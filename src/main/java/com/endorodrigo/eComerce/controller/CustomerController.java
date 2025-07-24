package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Controlador para la gestión de clientes.
 * Permite listar, crear, actualizar y eliminar clientes.
 */
@Controller
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public String customerPage(@RequestParam(value = "id", required = false) Integer id, Model model) {
        List<Customer> customers = customerService.getAll();
        Customer customer = (id != null) ? customerService.findId(id) : new Customer();
        model.addAttribute("customers", customers);
        model.addAttribute("customer", customer);
        return "customer";
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public String createCustomer(@Valid @ModelAttribute("customer") Customer customer, Errors errors, Model model) {
        logger.info("Creando cliente: {}", customer);
        if (errors.hasErrors()) {
            return "customer";
        }
        customerService.insert(customer);
        return "redirect:/customer";
    }

    public String updateCustomer(@ModelAttribute("customerForma") Customer customer) {
        logger.info("Actualizando cliente: {}", customer);
        return "redirect:/customer";
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findId(id);
        customerService.delete(customer);
        return "redirect:/customer";
    }

}
