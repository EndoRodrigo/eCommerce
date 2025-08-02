package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Items;
import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Items, Integer> {
    /**
     * Repositorio para la entidad Product.
     * Proporciona operaciones CRUD sobre productos.
     */
    Items findByCode(String code);
}
