package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.repository.ICustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements IGenericService<Customer, Integer> {

    private ICustomerRepository customerRepository;

    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findId(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Customer insert(Customer entity) {
        return customerRepository.save(entity);
    }

    @Override
    public void delete(Customer entity) {
        customerRepository.delete(entity);
    }
}
