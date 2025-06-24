package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Product;
import com.endorodrigo.eComerce.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IGenericService<Product, String> {

    private IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findId(String i) {
        return productRepository.findByCode(i);
    }

    @Override
    public List<Product> getAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product insert(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }
}
