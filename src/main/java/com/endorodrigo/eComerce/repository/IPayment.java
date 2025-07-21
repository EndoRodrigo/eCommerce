package com.endorodrigo.eComerce.repository;

import com.endorodrigo.eComerce.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface IPayment extends CrudRepository<Payment, Integer> {
}
