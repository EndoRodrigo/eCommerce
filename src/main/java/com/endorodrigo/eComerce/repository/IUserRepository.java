package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
