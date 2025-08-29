package com.endorodrigo.eCommerce.service;

import com.endorodrigo.eCommerce.exception.PosException;
import com.endorodrigo.eCommerce.exception.ValidationException;
import com.endorodrigo.eCommerce.model.Cart;
import com.endorodrigo.eCommerce.model.Item;
import com.endorodrigo.eCommerce.repository.IPosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
     * @throws ValidationException Si el carrito es nulo o no tiene items
     * @throws PosException Si hay un error al guardar en la base de datos
     */
    @Override
    public Cart insert(Cart entity) {
        try {
            // Validaciones
            if (entity == null) {
                throw new ValidationException("El carrito no puede ser nulo", "cart");
            }
            
            if (entity.getItems() == null || entity.getItems().isEmpty()) {
                throw new ValidationException("El carrito debe contener al menos un producto", "items");
            }
            
            if (entity.getCustomer() == null) {
                throw new ValidationException("El carrito debe tener un cliente asociado", "customer");
            }
            
            // Elimina duplicados y suma cantidades antes de guardar
            List<Item> uniqueItems = new ArrayList<>();
            for (Item item : entity.getItems()) {
                if (item == null || item.getCodeReference() == null) {
                    throw new ValidationException("Los productos del carrito no pueden ser nulos", "item");
                }
                
                boolean found = false;
                for (Item unique : uniqueItems) {
                    if (unique.getCodeReference().equals(item.getCodeReference())) {
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
            
            Cart savedCart = repository.save(entity);
            if (savedCart == null) {
                throw new PosException("Error al guardar el carrito en la base de datos", "DB_SAVE_ERROR");
            }
            
            return savedCart;
            
        } catch (ValidationException e) {
            throw e; // Re-lanzar excepciones de validación
        } catch (Exception e) {
            throw new PosException("Error inesperado al guardar el carrito: " + e.getMessage(), "UNEXPECTED_ERROR", e);
        }
    }

    /**
     * Actualiza un carrito existente, asegurando que no haya productos duplicados.
     * @param entity Carrito a actualizar
     * @return El carrito actualizado
     * @throws ValidationException Si el carrito es nulo o no tiene código de referencia
     * @throws PosException Si hay un error al actualizar en la base de datos
     */
    public Cart update(Cart entity) {
        try {
            if (entity == null) {
                throw new ValidationException("El carrito no puede ser nulo", "cart");
            }
            
            if (entity.getReference_code() == null) {
                throw new ValidationException("El carrito debe tener un código de referencia", "reference_code");
            }
            
            // Verificar que el carrito existe antes de actualizar
            // Como no tenemos findByReferenceCode, asumimos que si tiene ID existe
            if (entity.getId() == null) {
                throw new ValidationException("No se puede actualizar un carrito sin ID", "id");
            }
            
            // Elimina duplicados y suma cantidades antes de guardar
            List<Item> uniqueItems = new ArrayList<>();
            for (Item item : entity.getItems()) {
                if (item == null || item.getCodeReference() == null) {
                    throw new ValidationException("Los productos del carrito no pueden ser nulos", "item");
                }
                
                boolean found = false;
                for (Item unique : uniqueItems) {
                    if (unique.getCodeReference().equals(item.getCodeReference())) {
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
            
            Cart updatedCart = repository.save(entity);
            if (updatedCart == null) {
                throw new PosException("Error al actualizar el carrito en la base de datos", "DB_UPDATE_ERROR");
            }
            
            return updatedCart;
            
        } catch (ValidationException e) {
            throw e; // Re-lanzar excepciones de validación
        } catch (Exception e) {
            throw new PosException("Error inesperado al actualizar el carrito: " + e.getMessage(), "UNEXPECTED_ERROR", e);
        }
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

    public String generateIdCard(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

}
