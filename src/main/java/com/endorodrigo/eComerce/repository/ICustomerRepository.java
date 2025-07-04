package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
    Customer findById(Integer id);
}
