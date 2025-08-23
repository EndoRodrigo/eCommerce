package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Payment;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad Payment.
 * Proporciona operaciones CRUD sobre pagos.
 */
public interface IPayment extends CrudRepository<Payment, Integer> {
}
