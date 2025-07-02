package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.service.CustomerService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Controller
public class CustomerController {

    public static Logger log = (Logger) LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public String customerPage(@RequestParam(value = "id", required = false) Integer id, Model model) {
        List<Customer> customers = customerService.getAll();
        Customer customer;

        if (id != null) {
            customer = customerService.findId(id); // cliente existente
        } else {
            customer = new Customer(); // cliente nuevo
        }

        model.addAttribute("customers", customers);
        model.addAttribute("customer", customer);
        return "customer";
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public String createCustomer(@ModelAttribute("customerForma") Customer customer) {
        log.info("Customer create"+ customer);
        customerService.insert(customer);
        return "redirect:/customer";
    }

    public String updateCustomer(@ModelAttribute("customerForma") Customer customer) {
        log.info("Customer update"+ customer);
        return "redirect:/customer";
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findId(id);
        customerService.delete(customer);
        return "redirect:/customer";
    }

}
