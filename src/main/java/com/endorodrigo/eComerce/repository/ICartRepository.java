package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Cart (carrito de compras).
 * Proporciona operaciones CRUD y búsqueda por ID.
 */
@Repository
public interface ICartRepository extends CrudRepository<Cart, Integer> {
    /**
     * Busca un carrito por su identificador único.
     * @param id Identificador del carrito
     * @return Optional con el carrito si existe
     */
    Optional<Cart> findById(Integer id);
    
    /**
     * Busca carritos por cliente
     */
    java.util.List<Cart> findByCustomerId(Integer customerId);
}
