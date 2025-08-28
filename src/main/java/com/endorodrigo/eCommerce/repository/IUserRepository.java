package com.endorodrigo.eCommerce.repository;


import com.endorodrigo.eCommerce.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad User.
 * Proporciona operaciones CRUD sobre usuarios.
 */
public interface IUserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByEmail(String email);

}
