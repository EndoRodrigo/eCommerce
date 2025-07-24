package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findById(Integer id);
}
