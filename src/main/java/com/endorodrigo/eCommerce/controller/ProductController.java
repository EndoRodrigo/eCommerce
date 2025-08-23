package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Item;
import com.endorodrigo.eComerce.service.ItemService;
import com.endorodrigo.eComerce.service.NotificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión completa de productos
 * Incluye CRUD, búsqueda, filtros y gestión de inventario
 */
@Controller
@RequestMapping("/products")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ItemService itemService;
    private final NotificationService notificationService;

    public ProductController(ItemService itemService, NotificationService notificationService) {
        this.itemService = itemService;
        this.notificationService = notificationService;
    }

    /**
     * Lista paginada de productos con filtros
     */
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {
        
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), 
                sortBy != null ? sortBy : "name");
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Item> productsPage = itemService.findProductsWithFilters(search, category, pageable);
            
            model.addAttribute("products", productsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productsPage.getTotalPages());
            model.addAttribute("totalItems", productsPage.getTotalElements());
            model.addAttribute("search", search);
            model.addAttribute("category", category);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);
            
            // Estadísticas del inventario
            model.addAttribute("totalProducts", itemService.getTotalProducts());
            model.addAttribute("lowStockProducts", itemService.getLowStockProducts());
            model.addAttribute("outOfStockProducts", itemService.getOutOfStockProducts());
            
            return "products/list";
            
        } catch (Exception e) {
            logger.error("Error al listar productos", e);
            model.addAttribute("error", "Error al cargar los productos");
            return "products/list";
        }
    }

    /**
     * Vista de detalle de un producto
     */
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        try {
            Optional<Item> product = itemService.findById(id);
            if (product.isPresent()) {
                model.addAttribute("product", product.get());
                model.addAttribute("relatedProducts", itemService.getRelatedProducts(id));
                return "products/view";
            } else {
                model.addAttribute("error", "Producto no encontrado");
                return "redirect:/products";
            }
        } catch (Exception e) {
            logger.error("Error al mostrar producto con ID: {}", id, e);
            model.addAttribute("error", "Error al cargar el producto");
            return "redirect:/products";
        }
    }

    /**
     * Formulario para crear nuevo producto
     */
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Item());
        model.addAttribute("categories", itemService.getAllCategories());
        return "products/form";
    }

    /**
     * Crear nuevo producto
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createProduct(
            @Valid @ModelAttribute("product") Item product,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("categories", itemService.getAllCategories());
            return "products/form";
        }
        
        try {
            Item savedProduct = itemService.save(product);
            notificationService.sendProductCreatedNotification(savedProduct);
            
            redirectAttributes.addFlashAttribute("success", 
                "Producto '" + savedProduct.getName() + "' creado exitosamente");
            return "redirect:/products/" + savedProduct.getCode_reference();
            
        } catch (Exception e) {
            logger.error("Error al crear producto", e);
            model.addAttribute("error", "Error al crear el producto");
            model.addAttribute("categories", itemService.getAllCategories());
            return "products/form";
        }
    }

    /**
     * Formulario para editar producto
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String editProductForm(@PathVariable Long id, Model model) {
        try {
            Optional<Item> product = itemService.findById(id);
            if (product.isPresent()) {
                model.addAttribute("product", product.get());
                model.addAttribute("categories", itemService.getAllCategories());
                return "products/form";
            } else {
                return "redirect:/products";
            }
        } catch (Exception e) {
            logger.error("Error al cargar producto para editar con ID: {}", id, e);
            return "redirect:/products";
        }
    }

    /**
     * Actualizar producto
     */
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Item product,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("categories", itemService.getAllCategories());
            return "products/form";
        }
        
        try {
            product.setId(id);
            Item updatedProduct = itemService.save(product);
            notificationService.sendProductUpdatedNotification(updatedProduct);
            
            redirectAttributes.addFlashAttribute("success", 
                "Producto '" + updatedProduct.getName() + "' actualizado exitosamente");
            return "redirect:/products/" + updatedProduct.getId();
            
        } catch (Exception e) {
            logger.error("Error al actualizar producto con ID: {}", id, e);
            model.addAttribute("error", "Error al actualizar el producto");
            model.addAttribute("categories", itemService.getAllCategories());
            return "products/form";
        }
    }

    /**
     * Eliminar producto
     */
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Item> product = itemService.findById(id);
            if (product.isPresent()) {
                String productName = product.get().getName();
                itemService.deleteById(id);
                notificationService.sendProductDeletedNotification(productName);
                
                redirectAttributes.addFlashAttribute("success", 
                    "Producto '" + productName + "' eliminado exitosamente");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar producto con ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto");
        }
        
        return "redirect:/products";
    }

    /**
     * API REST para obtener productos (para AJAX)
     */
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Item>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<Item> products = itemService.searchProducts(query, limit);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error en búsqueda de productos", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para obtener productos por categoría
     */
    @GetMapping("/api/category/{category}")
    @ResponseBody
    public ResponseEntity<List<Item>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Item> productsPage = itemService.findByCategory(category, pageable);
            return ResponseEntity.ok(productsPage.getContent());
        } catch (Exception e) {
            logger.error("Error al obtener productos por categoría: {}", category, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para actualizar stock
     */
    @PostMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseBody
    public ResponseEntity<String> updateStock(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestParam String operation) {
        
        try {
            itemService.updateStock(id, quantity, operation);
            return ResponseEntity.ok("Stock actualizado exitosamente");
        } catch (Exception e) {
            logger.error("Error al actualizar stock del producto con ID: {}", id, e);
            return ResponseEntity.internalServerError().body("Error al actualizar stock");
        }
    }

    /**
     * Vista de inventario con alertas
     */
    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String inventoryView(Model model) {
        try {
            model.addAttribute("lowStockProducts", itemService.getLowStockProducts());
            model.addAttribute("outOfStockProducts", itemService.getOutOfStockProducts());
            model.addAttribute("inventoryStats", itemService.getInventoryStatistics());
            return "products/inventory";
        } catch (Exception e) {
            logger.error("Error al cargar vista de inventario", e);
            model.addAttribute("error", "Error al cargar el inventario");
            return "products/inventory";
        }
    }

    /**
     * Exportar productos a CSV/Excel
     */
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String exportProducts(
            @RequestParam(defaultValue = "csv") String format,
            RedirectAttributes redirectAttributes) {
        
        try {
            String filename = itemService.exportProducts(format);
            redirectAttributes.addFlashAttribute("success", 
                "Productos exportados exitosamente: " + filename);
        } catch (Exception e) {
            logger.error("Error al exportar productos", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar productos");
        }
        
        return "redirect:/products";
    }
}
