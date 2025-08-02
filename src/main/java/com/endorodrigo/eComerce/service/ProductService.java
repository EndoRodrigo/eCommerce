package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.controller.ProductController;
import com.endorodrigo.eComerce.model.Items;
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
public class ProductService implements IGenericService<Items, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    /**
     * Busca un producto por su código.
     * @param i Código del producto
     * @return Producto encontrado o null si no existe
     */
    public Items findId(String i) {
        if (i == null) return null;
        return productRepository.findByCode(i);
    }

    @Override
    /**
     * Obtiene todos los productos registrados.
     * @return Lista de productos
     */
    public List<Items> getAll() {
        return (List<Items>) productRepository.findAll();
    }

    @Override
    /**
     * Inserta o actualiza un producto en la base de datos.
     * @param entity Producto a guardar
     * @return Producto guardado
     */
    public Items insert(Items entity) {
        logger.info("Creacion producto capa service: {}", entity);
        return productRepository.save(entity);
    }

    @Override
    /**
     * Actualiza un producto en la base de datos.
     * @param entity Producto a actualizar
     * @return Producto actualizado
     */
    public Items update(Items entity) {
        logger.info("Actualizando producto capa service: {}", entity);
        return productRepository.save(entity);
    }

    @Override
    /**
     * Elimina un producto de la base de datos si no es nulo.
     * @param entity Producto a eliminar
     */
    public void delete(Items entity) {
        if (entity != null) productRepository.delete(entity);
    }
}
