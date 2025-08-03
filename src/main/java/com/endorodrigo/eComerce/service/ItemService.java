package com.endorodrigo.eComerce.service;


import com.endorodrigo.eComerce.model.Item;

import com.endorodrigo.eComerce.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para la gestión de productos.
 * Proporciona métodos CRUD para la entidad Product.
 */
@Service
public class ItemService implements IGenericService<Item, String> {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final IProductRepository productRepository;

    public ItemService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    /**
     * Busca un producto por su código.
     * @param i Código del producto
     * @return Producto encontrado o null si no existe
     */
    public Item findId(String i) {
        if (i == null) return null;
        return productRepository.findByCodeReference(i);
    }

    @Override
    /**
     * Obtiene todos los productos registrados.
     * @return Lista de productos
     */
    public List<Item> getAll() {
        return (List<Item>) productRepository.findAll();
    }

    @Override
    /**
     * Inserta o actualiza un producto en la base de datos.
     * @param entity Producto a guardar
     * @return Producto guardado
     */
    public Item insert(Item entity) {
        logger.info("Creacion producto capa service: {}", entity);
        return productRepository.save(entity);
    }

    @Override
    /**
     * Actualiza un producto en la base de datos.
     * @param entity Producto a actualizar
     * @return Producto actualizado
     */
    public Item update(Item entity) {
        logger.info("Actualizando producto capa service: {}", entity);
        return productRepository.save(entity);
    }

    @Override
    /**
     * Elimina un producto de la base de datos si no es nulo.
     * @param entity Producto a eliminar
     */
    public void delete(Item entity) {
        if (entity != null) productRepository.delete(entity);
    }
}
