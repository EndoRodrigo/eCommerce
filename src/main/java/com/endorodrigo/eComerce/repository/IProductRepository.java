package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Product, Integer> {
    Product findByCode(String code);
}
