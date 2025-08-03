package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Item, String> {
    /**
     * Repositorio para la entidad Product.
     * Proporciona operaciones CRUD sobre productos.
     */

}
