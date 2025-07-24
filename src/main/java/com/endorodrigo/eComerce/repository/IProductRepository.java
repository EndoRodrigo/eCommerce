package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Product, Integer> {
    /**
     * Repositorio para la entidad Product.
     * Proporciona operaciones CRUD sobre productos.
     */
    Product findByCode(String code);
}
