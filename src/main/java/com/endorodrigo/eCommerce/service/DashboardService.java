package com.endorodrigo.eComerce.service;


import com.endorodrigo.eCommerce.model.Cart;
import com.endorodrigo.eCommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICustomerRepository customerRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPosRepository posRepository;
    @Autowired
    private IPayment paymentRepository;

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
