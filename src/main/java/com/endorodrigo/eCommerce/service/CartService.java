package com.endorodrigo.eCommerce.service;

import com.endorodrigo.eComerce.model.CartItem;
import com.endorodrigo.eComerce.model.CartSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Servicio para manejar el carrito de compras del sistema eCommerce
 * Incluye gestión de sesiones de carrito, agregar/eliminar productos y cálculos
 */
@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    
    // Almacenamiento temporal de sesiones de carrito (en producción usar Redis o base de datos)
    private final Map<String, CartSession> cartSessions = new ConcurrentHashMap<>();

    /**
     * Obtener sesión de carrito por ID de sesión
     */
    public CartSession getCartSession(String sessionId) {
        try {
            CartSession cartSession = cartSessions.get(sessionId);
            if (cartSession == null) {
                // Crear nueva sesión de carrito si no existe
                cartSession = new CartSession();
                cartSession.setSessionId(sessionId);
                cartSession.setItems(new ArrayList<>());
                cartSession.setCreatedAt(java.time.LocalDateTime.now());
                cartSessions.put(sessionId, cartSession);
            }
            return cartSession;
            
        } catch (Exception e) {
            logger.error("Error al obtener sesión de carrito para: {}", sessionId, e);
            return null;
        }
    }

    /**
     * Agregar producto al carrito
     */
    public boolean addToCart(String sessionId, CartItem cartItem) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            // Verificar si el producto ya existe en el carrito
            CartItem existingItem = findCartItem(cartSession, cartItem.getProductId());
            
            if (existingItem != null) {
                // Actualizar cantidad si el producto ya existe
                int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();
                existingItem.setQuantity(newQuantity);
                
                logger.info("Cantidad actualizada para producto {} en carrito: {}", 
                    cartItem.getProductName(), newQuantity);
            } else {
                // Agregar nuevo producto al carrito
                cartSession.addItem(cartItem);
                
                logger.info("Producto {} agregado al carrito: {}", 
                    cartItem.getProductName(), cartItem.getQuantity());
            }

            // Actualizar timestamp de la sesión
            cartSession.setUpdatedAt(java.time.LocalDateTime.now());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error al agregar producto al carrito: {}", cartItem.getProductName(), e);
            return false;
        }
    }

    /**
     * Actualizar cantidad de un producto en el carrito
     */
    public boolean updateCartItemQuantity(String sessionId, Long productId, int newQuantity) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            CartItem existingItem = findCartItem(cartSession, productId);
            if (existingItem == null) {
                logger.warn("Producto {} no encontrado en carrito para sesión: {}", productId, sessionId);
                return false;
            }

            // Actualizar cantidad
            existingItem.setQuantity(newQuantity);

            // Actualizar timestamp de la sesión
            cartSession.setUpdatedAt(java.time.LocalDateTime.now());

            logger.info("Cantidad actualizada para producto {} en carrito: {}", 
                existingItem.getProductName(), newQuantity);
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error al actualizar cantidad del producto {} en carrito", productId, e);
            return false;
        }
    }

    /**
     * Eliminar producto del carrito
     */
    public boolean removeFromCart(String sessionId, Long productId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            // Verificar si el producto existe antes de removerlo
            boolean existed = cartSession.containsProduct(productId);
            cartSession.removeItemById(productId);

            if (existed) {
                // Actualizar timestamp de la sesión
                cartSession.setUpdatedAt(java.time.LocalDateTime.now());
                
                logger.info("Producto {} eliminado del carrito para sesión: {}", productId, sessionId);
                return true;
            } else {
                logger.warn("Producto {} no encontrado en carrito para sesión: {}", productId, sessionId);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error al eliminar producto {} del carrito", productId, e);
            return false;
        }
    }

    /**
     * Limpiar carrito completo
     */
    public boolean clearCart(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            cartSession.clearItems();
            cartSession.setUpdatedAt(java.time.LocalDateTime.now());

            logger.info("Carrito limpiado para sesión: {}", sessionId);
            return true;
            
        } catch (Exception e) {
            logger.error("Error al limpiar carrito para sesión: {}", sessionId, e);
            return false;
        }
    }

    /**
     * Obtener total del carrito
     */
    public BigDecimal getCartTotal(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null || cartSession.getItems().isEmpty()) {
                return BigDecimal.ZERO;
            }

            return cartSession.getSubtotal();
                
        } catch (Exception e) {
            logger.error("Error al calcular total del carrito para sesión: {}", sessionId, e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Obtener número de items en el carrito
     */
    public int getCartItemCount(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                return 0;
            }

            return cartSession.getTotalQuantity();
                
        } catch (Exception e) {
            logger.error("Error al contar items del carrito para sesión: {}", sessionId, e);
            return 0;
        }
    }

    /**
     * Obtener número de productos únicos en el carrito
     */
    public int getCartUniqueItemCount(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                return 0;
            }

            return cartSession.getItemCount();
                
        } catch (Exception e) {
            logger.error("Error al contar productos únicos del carrito para sesión: {}", sessionId, e);
            return 0;
        }
    }

    /**
     * Verificar si el carrito está vacío
     */
    public boolean isCartEmpty(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            return cartSession == null || cartSession.isEmpty();
                
        } catch (Exception e) {
            logger.error("Error al verificar si el carrito está vacío para sesión: {}", sessionId, e);
            return true;
        }
    }

    /**
     * Obtener resumen del carrito
     */
    public Map<String, Object> getCartSummary(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                return Map.of(
                    "isEmpty", true,
                    "itemCount", 0,
                    "uniqueItemCount", 0,
                    "total", BigDecimal.ZERO,
                    "items", List.of()
                );
            }

            BigDecimal total = getCartTotal(sessionId);
            int itemCount = getCartItemCount(sessionId);
            int uniqueItemCount = getCartUniqueItemCount(sessionId);

            return Map.of(
                "isEmpty", cartSession.isEmpty(),
                "itemCount", itemCount,
                "uniqueItemCount", uniqueItemCount,
                "total", total,
                "items", cartSession.getItems(),
                "createdAt", cartSession.getCreatedAt(),
                "updatedAt", cartSession.getUpdatedAt()
            );
                
        } catch (Exception e) {
            logger.error("Error al obtener resumen del carrito para sesión: {}", sessionId, e);
            return Map.of("error", "Error al obtener resumen del carrito");
        }
    }

    /**
     * Aplicar descuento al carrito
     */
    public boolean applyDiscount(String sessionId, String discountCode, BigDecimal discountAmount) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            // Validar descuento
            if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("Monto de descuento inválido: {}", discountAmount);
                return false;
            }

            BigDecimal cartTotal = getCartTotal(sessionId);
            if (discountAmount.compareTo(cartTotal) > 0) {
                logger.warn("El descuento no puede ser mayor al total del carrito");
                return false;
            }

            // Aplicar descuento
            cartSession.applyDiscount(discountCode, discountAmount);

            logger.info("Descuento aplicado al carrito: {} - ${}", discountCode, discountAmount);
            return true;
                
        } catch (Exception e) {
            logger.error("Error al aplicar descuento al carrito para sesión: {}", sessionId, e);
            return false;
        }
    }

    /**
     * Remover descuento del carrito
     */
    public boolean removeDiscount(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null) {
                logger.error("No se pudo obtener sesión de carrito para: {}", sessionId);
                return false;
            }

            cartSession.removeDiscount();

            logger.info("Descuento removido del carrito para sesión: {}", sessionId);
            return true;
                
        } catch (Exception e) {
            logger.error("Error al remover descuento del carrito para sesión: {}", sessionId, e);
            return false;
        }
    }

    /**
     * Obtener total con descuento aplicado
     */
    public BigDecimal getCartTotalWithDiscount(String sessionId) {
        try {
            BigDecimal cartTotal = getCartTotal(sessionId);
            CartSession cartSession = getCartSession(sessionId);
            
            if (cartSession != null && cartSession.getDiscountAmount() != null) {
                return cartTotal.subtract(cartSession.getDiscountAmount()).max(BigDecimal.ZERO);
            }
            
            return cartTotal;
                
        } catch (Exception e) {
            logger.error("Error al calcular total con descuento para sesión: {}", sessionId, e);
            return getCartTotal(sessionId);
        }
    }

    /**
     * Validar stock de productos en el carrito
     */
    public Map<String, Object> validateCartStock(String sessionId) {
        try {
            CartSession cartSession = getCartSession(sessionId);
            if (cartSession == null || cartSession.getItems().isEmpty()) {
                return Map.of("valid", true, "issues", List.of());
            }

            List<Map<String, Object>> issues = new ArrayList<>();
            boolean isValid = true;

            for (CartItem item : cartSession.getItems()) {
                // Aquí se debería verificar el stock real contra la base de datos
                // Por ahora, simulamos la validación
                if (item.getQuantity() > 100) { // Simular stock máximo
                    issues.add(Map.of(
                        "productId", item.getProductId(),
                        "productName", item.getProductName(),
                        "issue", "Stock insuficiente",
                        "requested", item.getQuantity(),
                        "available", 100
                    ));
                    isValid = false;
                }
            }

            return Map.of(
                "valid", isValid,
                "issues", issues
            );
                
        } catch (Exception e) {
            logger.error("Error al validar stock del carrito para sesión: {}", sessionId, e);
            return Map.of("valid", false, "error", "Error al validar stock");
        }
    }

    /**
     * Limpiar sesiones de carrito expiradas
     */
    public void cleanupExpiredSessions() {
        try {
            java.time.LocalDateTime expirationTime = java.time.LocalDateTime.now().minusHours(24);
            
            List<String> expiredSessions = cartSessions.entrySet().stream()
                .filter(entry -> {
                    CartSession session = entry.getValue();
                    return session.getUpdatedAt() != null && 
                           session.getUpdatedAt().isBefore(expirationTime);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            for (String sessionId : expiredSessions) {
                cartSessions.remove(sessionId);
                logger.info("Sesión de carrito expirada removida: {}", sessionId);
            }

            logger.info("Limpieza de sesiones expiradas completada. {} sesiones removidas", expiredSessions.size());
                
        } catch (Exception e) {
            logger.error("Error al limpiar sesiones expiradas", e);
        }
    }

    /**
     * Obtener estadísticas del carrito
     */
    public Map<String, Object> getCartStatistics() {
        try {
            int totalSessions = cartSessions.size();
            int activeSessions = (int) cartSessions.values().stream()
                .filter(session -> !session.isEmpty())
                .count();

            long totalItems = cartSessions.values().stream()
                .mapToLong(session -> session.getItemCount())
                .sum();

            BigDecimal totalValue = cartSessions.values().stream()
                .map(session -> getCartTotal(session.getSessionId()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            return Map.of(
                "totalSessions", totalSessions,
                "activeSessions", activeSessions,
                "totalItems", totalItems,
                "totalValue", totalValue,
                "averageItemsPerSession", totalSessions > 0 ? (double) totalItems / totalSessions : 0.0,
                "averageValuePerSession", totalSessions > 0 ? totalValue.divide(BigDecimal.valueOf(totalSessions), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO
            );
                
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas del carrito", e);
            return Map.of("error", "Error al obtener estadísticas");
        }
    }

    /**
     * Métodos auxiliares privados
     */
    private CartItem findCartItem(CartSession cartSession, Long productId) {
        return cartSession.findItemById(productId);
    }

    private BigDecimal getCartTotal(CartSession cartSession) {
        if (cartSession == null || cartSession.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cartSession.getSubtotal();
    }
}
