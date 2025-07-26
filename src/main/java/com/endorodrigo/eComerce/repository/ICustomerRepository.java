
package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface ICustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("SELECT COALESCE(MAX(c.id), 0) FROM Customer c")
    Integer findMaxId();

}
