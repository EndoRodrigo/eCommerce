package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPosRepository extends CrudRepository<Cart, Integer> {
    Optional<Cart> findById(Integer id);
}
