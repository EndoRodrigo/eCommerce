package com.endorodrigo.eCommerce.service;


import com.endorodrigo.eCommerce.model.Cart;
import com.endorodrigo.eCommerce.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final IProductRepository productRepository;

    private final ICustomerRepository customerRepository;

    private final IUserRepository userRepository;

    private final IPosRepository posRepository;

    private final IPayment paymentRepository;

    public DashboardService(IProductRepository productRepository, ICustomerRepository customerRepository, IUserRepository userRepository, IPosRepository posRepository, IPayment paymentRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.posRepository = posRepository;
        this.paymentRepository = paymentRepository;
    }

    public long getTotalVentas() {
        return paymentRepository.count();
    }

    public long getTotalProductos() {
        return productRepository.count();
    }

    public long getTotalClientes() {
        return customerRepository.count();
    }

    public long getTotalUsuarios() {
        return userRepository.count();
    }

    public long getTotalCarritos() {
        return posRepository.count();
    }
    // Puedes agregar más métodos según los datos disponibles

    public List<Cart> getCart(){
        return (List<Cart>) posRepository.findAll();
    }
}
