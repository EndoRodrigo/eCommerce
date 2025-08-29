package com.endorodrigo.eCommerce.repository;

import com.endorodrigo.eCommerce.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductRepository extends CrudRepository<Item, Long> {
    /**
     * Repositorio para la entidad Product.
     * Proporciona operaciones CRUD sobre productos.
     */
    /**
     * Busca productos por nombre (ignorando mayúsculas/minúsculas)
     */
    Page<Item> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Busca productos con cantidad menor a un valor
     */
    java.util.List<Item> findByQuantityLessThan(Integer quantity);
    
    /**
     * Busca productos con cantidad específica
     */
    java.util.List<Item> findByQuantity(Integer quantity);


    Item findByCodeReference(String code_reference);
}
