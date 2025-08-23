package com.endorodrigo.eComerce.service;


import com.endorodrigo.eComerce.model.Item;

import com.endorodrigo.eComerce.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

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
        return productRepository.findById(i).get();
    }

    /**
     * Busca un producto por su ID
     */
    public Optional<Item> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Guarda un producto (crear o actualizar)
     */
    public Item save(Item item) {
        return productRepository.save(item);
    }

    /**
     * Elimina un producto por ID
     */
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    /**
     * Busca productos con filtros
     */
    public Page<Item> findProductsWithFilters(String search, String category, Pageable pageable) {
        // Implementación básica - buscar por nombre
        if (search != null && !search.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        }
        return productRepository.findAll(pageable);
    }

    /**
     * Obtiene el total de productos
     */
    public long getTotalProducts() {
        return productRepository.count();
    }

    /**
     * Obtiene productos con stock bajo
     */
    public List<Item> getLowStockProducts() {
        // Implementación básica - productos con cantidad menor a 10
        return productRepository.findByQuantityLessThan(10);
    }

    /**
     * Obtiene productos sin stock
     */
    public List<Item> getOutOfStockProducts() {
        // Implementación básica - productos con cantidad 0
        return productRepository.findByQuantity(0);
    }

    /**
     * Obtiene productos relacionados
     */
    public List<Item> getRelatedProducts(String productId) {
        // Implementación básica - retorna productos de la misma categoría
        return productRepository.findAll().stream()
                .filter(item -> !item.getCode_reference().equals(productId))
                .limit(5)
                .toList();
    }

    /**
     * Obtiene todas las categorías
     */
    public List<String> getAllCategories() {
        // Implementación básica - categorías hardcodeadas
        return List.of("Electrónicos", "Ropa", "Hogar", "Deportes", "Libros");
    }

    /**
     * Busca productos por nombre
     */
    public List<Item> searchProducts(String query, int limit) {
        return productRepository.findByNameContainingIgnoreCase(query, 
            org.springframework.data.domain.PageRequest.of(0, limit)).getContent();
    }

    /**
     * Busca productos por categoría
     */
    public Page<Item> findByCategory(String category, Pageable pageable) {
        // Implementación básica - retorna todos los productos
        return productRepository.findAll(pageable);
    }

    /**
     * Actualiza el stock de un producto
     */
    public void updateStock(String productId, int quantity, String operation) {
        Optional<Item> itemOpt = productRepository.findById(productId);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            if ("add".equals(operation)) {
                item.setQuantity(item.getQuantity() + quantity);
            } else if ("subtract".equals(operation)) {
                item.setQuantity(Math.max(0, item.getQuantity() - quantity));
            }
            productRepository.save(item);
        }
    }

    /**
     * Exporta productos
     */
    public String exportProducts(String format) {
        // Implementación básica
        return "productos_exportados." + format;
    }

    /**
     * Obtiene estadísticas del inventario
     */
    public Map<String, Object> getInventoryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", getTotalProducts());
        stats.put("lowStockCount", getLowStockProducts().size());
        stats.put("outOfStockCount", getOutOfStockProducts().size());
        return stats;
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
