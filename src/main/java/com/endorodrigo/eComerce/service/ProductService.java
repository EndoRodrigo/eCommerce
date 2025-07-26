package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Product;
import com.endorodrigo.eComerce.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de productos.
 * Proporciona métodos CRUD para la entidad Product.
 */
@Service
public class ProductService implements IGenericService<Product, String> {

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
    public Product findId(String i) {
        if (i == null) return null;
        return productRepository.findByCode(i);
    }

    @Override
    /**
     * Obtiene todos los productos registrados.
     * @return Lista de productos
     */
    public List<Product> getAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    /**
     * Inserta o actualiza un producto en la base de datos.
     * @param entity Producto a guardar
     * @return Producto guardado
     */
    public Product insert(Product entity) {
        if (entity == null) return null;
        // Validar unicidad de código
        Product existing = productRepository.findByCode(entity.getCode());
        if (existing != null && (entity.getId() == null || !existing.getId().equals(entity.getId()))) {
            throw new RuntimeException("Ya existe un producto con el código: " + entity.getCode());
        }
        return productRepository.save(entity);
    }

    @Override
    /**
     * Actualiza un producto en la base de datos.
     * @param entity Producto a actualizar
     * @return Producto actualizado
     */
    public Product update(Product entity) {
        if (entity == null || entity.getId() == null) return null;
        Product existing = productRepository.findByCode(entity.getCode());
        if (existing != null && !existing.getId().equals(entity.getId())) {
            throw new RuntimeException("Ya existe un producto con el código: " + entity.getCode());
        }
        return productRepository.save(entity);
    }

    @Override
    /**
     * Elimina un producto de la base de datos si no es nulo.
     * @param entity Producto a eliminar
     */
    public void delete(Product entity) {
        if (entity != null) productRepository.delete(entity);
    }
}
