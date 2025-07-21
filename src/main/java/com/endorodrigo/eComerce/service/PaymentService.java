package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Payment;
import com.endorodrigo.eComerce.repository.IPayment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IGenericService<Payment, Integer>{

    private IPayment paymentRepository;

    public PaymentService(IPayment paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findId(Integer integer) {
        return null;
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    @Override
    public Payment insert(Payment entity) {
        return paymentRepository.save(entity);
    }

    @Override
    public void delete(Payment entity) {

    }
}
