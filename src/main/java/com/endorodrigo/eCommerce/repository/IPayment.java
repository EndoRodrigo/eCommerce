package com.endorodrigo.eCommerce.repository;

import com.endorodrigo.eCommerce.model.Payment;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad Payment.
 * Proporciona operaciones CRUD sobre pagos.
 */
public interface IPayment extends CrudRepository<Payment, Integer> {
}
