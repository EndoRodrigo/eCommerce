package com.endorodrigo.eCommerce.repository;




import java.util.Optional;

import com.endorodrigo.eCommerce.model.Customer;
import org.springframework.data.repository.CrudRepository;



public interface ICustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findById(Integer id);
    Customer findByIdentification(String identification);
}
