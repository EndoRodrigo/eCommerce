package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Cart;
import com.endorodrigo.eComerce.model.CartItem;
import com.endorodrigo.eComerce.repository.IPosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para la gestión de carritos de compra (POS).
 * Proporciona métodos CRUD para la entidad Cart.
 */
/**
 * Servicio para la gestión de carritos de compra (POS).
 * Proporciona métodos CRUD para la entidad Cart.
 */
@Service
public class PosService implements IGenericService<Cart, Integer>{


    /**
     * Repositorio para operaciones CRUD sobre la entidad Cart.
     */
    private final IPosRepository repository;

    /**
     * Constructor que inyecta el repositorio de carritos.
     * @param repository Repositorio de carritos
     */
    public PosService(IPosRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca un carrito por su ID.
     * Si el ID es nulo, retorna null.
     * @param id Identificador del carrito
     * @return El carrito si existe, null si no se encuentra o el id es nulo
     */
    @Override
    public Cart findId(Integer id) {
        if (id == null) return null;
        return repository.findById(id).orElse(null);
    }

    /**
     * Obtiene todos los carritos registrados en el sistema.
     * @return Lista de carritos
     */
    @Override
    public List<Cart> getAll() {
        return (List<Cart>) repository.findAll();
    }

    /**
     * Inserta o actualiza un carrito en la base de datos.
     * Si el carrito ya existe, lo actualiza; si no, lo crea.
     * @param entity Carrito a guardar
     * @return Carrito guardado
     */
    @Override
    public Cart insert(Cart entity) {
        // Elimina duplicados y suma cantidades antes de guardar
        List<CartItem> uniqueItems = new ArrayList<>();
        for (CartItem item : entity.getItems()) {
            boolean found = false;
            for (CartItem unique : uniqueItems) {
                if (unique.getProduct().getId().equals(item.getProduct().getId())) {
                    unique.setQuantity(unique.getQuantity() + item.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                uniqueItems.add(item);
            }
        }
        entity.setItems(uniqueItems);
        return repository.save(entity);
    }

    /**
     * Actualiza un carrito existente, asegurando que no haya productos duplicados.
     * @param entity Carrito a actualizar
     * @return El carrito actualizado, o null si el carrito no existe
     */
    public Cart update(Cart entity) {
        if (entity == null || entity.getIdCar() == null) return null;
        // Elimina duplicados y suma cantidades antes de guardar
        List<CartItem> uniqueItems = new ArrayList<>();
        for (CartItem item : entity.getItems()) {
            boolean found = false;
            for (CartItem unique : uniqueItems) {
                if (unique.getProduct().getId().equals(item.getProduct().getId())) {
                    unique.setQuantity(unique.getQuantity() + item.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                uniqueItems.add(item);
            }
        }
        entity.setItems(uniqueItems);
        return repository.save(entity);
    }

    /**
     * Elimina un carrito de la base de datos si no es nulo.
     * Si el carrito es null, no realiza ninguna acción.
     * @param entity Carrito a eliminar
     */
    @Override
    public void delete(Cart entity) {
        if (entity != null) repository.delete(entity);
    }
}
