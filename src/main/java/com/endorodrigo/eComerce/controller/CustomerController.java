package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class CustomerController {

    Logger log = Logger.getLogger(String.valueOf(CustomerController.class));

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer/all", method = RequestMethod.GET)
    public List<Customer> getCustomers() {
        List<Customer> data = customerService.getAll();
        return data;
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable Integer id) {
        return customerService.findId(id);
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public Customer createCustomer(@RequestBody Customer customer) {
        log.info("createCustomer"+ customer);
        return customerService.insert(customer);
    }

    @RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.GET)
    public Customer deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findId(id);
        customerService.delete(customer);
        return customer;
    }

}
