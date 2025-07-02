package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Cart;
import com.endorodrigo.eComerce.repository.IPosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosService implements IGenericService<Cart, Integer>{


    private IPosRepository repository;

    public PosService(IPosRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart findId(Integer integer) {
        return null;
    }

    @Override
    public List<Cart> getAll() {
        return List.of();
    }

    @Override
    public Cart insert(Cart entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Cart entity) {

    }
}
