package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.CartSession;
import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.model.Item;
import com.endorodrigo.eComerce.model.Payment;
import com.endorodrigo.eComerce.repository.IProductRepository;
import com.endorodrigo.eComerce.repository.ICustomerRepository;
import com.endorodrigo.eComerce.repository.IPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Servicio para manejar ventas y órdenes del sistema eCommerce
 * Incluye creación, gestión y seguimiento de órdenes
 */
@Service
public class SalesService {

    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);
    
    @Autowired
    private IProductRepository productRepository;
    
    @Autowired
    private ICustomerRepository customerRepository;
    
    @Autowired
    private IPayment paymentRepository;

    // Almacenamiento temporal de órdenes (en producción usar base de datos)
    private final Map<String, Map<String, Object>> orders = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> orderItems = new ConcurrentHashMap<>();

    /**
     * Crear nueva orden
     */
    public String createOrder(CartSession cartSession, Customer customer, String shippingMethod) {
        try {
            // Generar número de orden único
            String orderNumber = generateOrderNumber();
            
            // Calcular totales
            BigDecimal subtotal = calculateSubtotal(cartSession);
            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.08)); // 8% IVA
            BigDecimal shipping = getShippingCost(shippingMethod);
            BigDecimal finalTotal = subtotal.add(tax).add(shipping);
            
            // Crear orden
            Map<String, Object> order = new HashMap<>();
            order.put("orderNumber", orderNumber);
            order.put("customerId", customer.getId());
            order.put("customerName", customer.getNames());
            order.put("shippingAddress", customer.getAddress());
            order.put("shippingMethod", shippingMethod);
            order.put("shippingCost", shipping);
            order.put("subtotal", subtotal);
            order.put("tax", tax);
            order.put("total", finalTotal);
            order.put("status", "PENDING");
            order.put("createdAt", LocalDateTime.now());
            order.put("updatedAt", LocalDateTime.now());
            
            // Guardar orden
            orders.put(orderNumber, order);
            
            // Guardar items de la orden
            List<Map<String, Object>> items = new ArrayList<>();
            for (var cartItem : cartSession.getItems()) {
                Map<String, Object> orderItem = new HashMap<>();
                orderItem.put("productId", cartItem.getProductId());
                orderItem.put("productName", cartItem.getProductName());
                orderItem.put("price", cartItem.getPrice());
                orderItem.put("quantity", cartItem.getQuantity());
                orderItem.put("subtotal", cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                orderItem.put("image", cartItem.getImage());
                items.add(orderItem);
            }
            orderItems.put(orderNumber, items);
            
            logger.info("Orden creada exitosamente: {}", orderNumber);
            return orderNumber;
            
        } catch (Exception e) {
            logger.error("Error al crear orden", e);
            throw new RuntimeException("Error al crear la orden", e);
        }
    }

    /**
     * Obtener detalles de una orden
     */
    public Map<String, Object> getOrderDetails(String orderNumber) {
        try {
            Map<String, Object> order = orders.get(orderNumber);
            if (order == null) {
                return null;
            }
            
            // Crear copia de la orden con items
            Map<String, Object> orderDetails = new HashMap<>(order);
            orderDetails.put("items", orderItems.getOrDefault(orderNumber, List.of()));
            
            return orderDetails;
            
        } catch (Exception e) {
            logger.error("Error al obtener detalles de orden: {}", orderNumber, e);
            return null;
        }
    }

    /**
     * Obtener todas las órdenes
     */
    public List<Map<String, Object>> getAllOrders() {
        try {
            List<Map<String, Object>> allOrders = new ArrayList<>();
            
            for (Map.Entry<String, Map<String, Object>> entry : orders.entrySet()) {
                String orderNumber = entry.getKey();
                Map<String, Object> order = entry.getValue();
                
                // Agregar items a cada orden
                Map<String, Object> orderWithItems = new HashMap<>(order);
                orderWithItems.put("items", orderItems.getOrDefault(orderNumber, List.of()));
                allOrders.add(orderWithItems);
            }
            
            // Ordenar por fecha de creación (más reciente primero)
            allOrders.sort((o1, o2) -> {
                LocalDateTime date1 = (LocalDateTime) o1.get("createdAt");
                LocalDateTime date2 = (LocalDateTime) o2.get("createdAt");
                return date2.compareTo(date1);
            });
            
            return allOrders;
            
        } catch (Exception e) {
            logger.error("Error al obtener todas las órdenes", e);
            return List.of();
        }
    }

    /**
     * Obtener órdenes por estado
     */
    public List<Map<String, Object>> getOrdersByStatus(String status) {
        try {
            return getAllOrders().stream()
                .filter(order -> status.equals(order.get("status")))
                .toList();
                
        } catch (Exception e) {
            logger.error("Error al obtener órdenes por estado: {}", status, e);
            return List.of();
        }
    }

    /**
     * Obtener órdenes por cliente
     */
    public List<Map<String, Object>> getOrdersByCustomer(Long customerId) {
        try {
            return getAllOrders().stream()
                .filter(order -> customerId.equals(order.get("customerId")))
                .toList();
                
        } catch (Exception e) {
            logger.error("Error al obtener órdenes por cliente: {}", customerId, e);
            return List.of();
        }
    }

    /**
     * Obtener órdenes por rango de fechas
     */
    public List<Map<String, Object>> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return getAllOrders().stream()
                .filter(order -> {
                    LocalDateTime orderDate = (LocalDateTime) order.get("createdAt");
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .toList();
                
        } catch (Exception e) {
            logger.error("Error al obtener órdenes por rango de fechas", e);
            return List.of();
        }
    }

    /**
     * Actualizar estado de una orden
     */
    public boolean updateOrderStatus(String orderNumber, String newStatus) {
        try {
            Map<String, Object> order = orders.get(orderNumber);
            if (order == null) {
                logger.warn("Orden no encontrada: {}", orderNumber);
                return false;
            }
            
            // Validar estado válido
            if (!isValidOrderStatus(newStatus)) {
                logger.warn("Estado de orden inválido: {}", newStatus);
                return false;
            }
            
            // Actualizar estado
            order.put("status", newStatus);
            order.put("updatedAt", LocalDateTime.now());
            
            // Si la orden se cancela, restaurar inventario
            if ("CANCELLED".equals(newStatus)) {
                restoreInventory(orderNumber);
            }
            
            logger.info("Estado de orden {} actualizado a: {}", orderNumber, newStatus);
            return true;
            
        } catch (Exception e) {
            logger.error("Error al actualizar estado de orden: {}", orderNumber, e);
            return false;
        }
    }

    /**
     * Cancelar orden
     */
    public boolean cancelOrder(String orderNumber) {
        try {
            Map<String, Object> order = orders.get(orderNumber);
            if (order == null) {
                logger.warn("Orden no encontrada: {}", orderNumber);
                return false;
            }
            
            String currentStatus = (String) order.get("status");
            if ("CANCELLED".equals(currentStatus) || "SHIPPED".equals(currentStatus)) {
                logger.warn("No se puede cancelar la orden {} con estado: {}", orderNumber, currentStatus);
                return false;
            }
            
            // Actualizar estado a cancelado
            return updateOrderStatus(orderNumber, "CANCELLED");
            
        } catch (Exception e) {
            logger.error("Error al cancelar orden: {}", orderNumber, e);
            return false;
        }
    }

    /**
     * Marcar orden como enviada
     */
    public boolean shipOrder(String orderNumber, String trackingNumber) {
        try {
            Map<String, Object> order = orders.get(orderNumber);
            if (order == null) {
                logger.warn("Orden no encontrada: {}", orderNumber);
                return false;
            }
            
            String currentStatus = (String) order.get("status");
            if (!"PAID".equals(currentStatus)) {
                logger.warn("No se puede enviar la orden {} con estado: {}", orderNumber, currentStatus);
                return false;
            }
            
            // Agregar número de seguimiento
            order.put("trackingNumber", trackingNumber);
            order.put("shippedAt", LocalDateTime.now());
            
            // Actualizar estado a enviado
            return updateOrderStatus(orderNumber, "SHIPPED");
            
        } catch (Exception e) {
            logger.error("Error al enviar orden: {}", orderNumber, e);
            return false;
        }
    }

    /**
     * Marcar orden como entregada
     */
    public boolean deliverOrder(String orderNumber) {
        try {
            Map<String, Object> order = orders.get(orderNumber);
            if (order == null) {
                logger.warn("Orden no encontrada: {}", orderNumber);
                return false;
            }
            
            String currentStatus = (String) order.get("status");
            if (!"SHIPPED".equals(currentStatus)) {
                logger.warn("No se puede marcar como entregada la orden {} con estado: {}", orderNumber, currentStatus);
                return false;
            }
            
            // Agregar fecha de entrega
            order.put("deliveredAt", LocalDateTime.now());
            
            // Actualizar estado a entregado
            return updateOrderStatus(orderNumber, "DELIVERED");
            
        } catch (Exception e) {
            logger.error("Error al marcar orden como entregada: {}", orderNumber, e);
            return false;
        }
    }

    /**
     * Obtener estadísticas de ventas
     */
    public Map<String, Object> getSalesStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Map<String, Object>> ordersInRange = getOrdersByDateRange(startDate, endDate);
            
            // Calcular estadísticas
            BigDecimal totalRevenue = ordersInRange.stream()
                .map(order -> (BigDecimal) order.get("total"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            int totalOrders = ordersInRange.size();
            
            BigDecimal averageOrderValue = totalOrders > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
            
            // Contar órdenes por estado
            Map<String, Long> ordersByStatus = ordersInRange.stream()
                .collect(Collectors.groupingBy(
                    order -> (String) order.get("status"),
                    Collectors.counting()
                ));
            
            // Productos más vendidos
            Map<Long, Integer> productSales = new HashMap<>();
            for (Map<String, Object> order : ordersInRange) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
                for (Map<String, Object> item : items) {
                    Long productId = (Long) item.get("productId");
                    Integer quantity = (Integer) item.get("quantity");
                    productSales.merge(productId, quantity, Integer::sum);
                }
            }
            
            // Top 5 productos más vendidos
            List<Map<String, Object>> topProducts = productSales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", entry.getKey());
                    product.put("quantity", entry.getValue());
                    return product;
                })
                .toList();
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalRevenue", totalRevenue);
            statistics.put("totalOrders", totalOrders);
            statistics.put("averageOrderValue", averageOrderValue);
            statistics.put("ordersByStatus", ordersByStatus);
            statistics.put("topProducts", topProducts);
            statistics.put("startDate", startDate);
            statistics.put("endDate", endDate);
            
            return statistics;
            
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas de ventas", e);
            return Map.of("error", "Error al obtener estadísticas");
        }
    }

    /**
     * Obtener estadísticas de ventas del día
     */
    public Map<String, Object> getTodaySalesStatistics() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return getSalesStatistics(startOfDay, endOfDay);
    }

    /**
     * Obtener estadísticas de ventas del mes
     */
    public Map<String, Object> getMonthlySalesStatistics() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(
            LocalDateTime.now().getMonth().length(LocalDateTime.now().toLocalDate().isLeapYear())
        ).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return getSalesStatistics(startOfMonth, endOfMonth);
    }

    /**
     * Obtener estadísticas de ventas del año
     */
    public Map<String, Object> getYearlySalesStatistics() {
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfYear = LocalDateTime.now().withDayOfYear(
            LocalDateTime.now().toLocalDate().isLeapYear() ? 366 : 365
        ).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return getSalesStatistics(startOfYear, endOfYear);
    }

    /**
     * Generar reporte de ventas
     */
    public Map<String, Object> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate, String format) {
        try {
            Map<String, Object> statistics = getSalesStatistics(startDate, endDate);
            List<Map<String, Object>> ordersInRange = getOrdersByDateRange(startDate, endDate);
            
            Map<String, Object> report = new HashMap<>();
            report.put("statistics", statistics);
            report.put("orders", ordersInRange);
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("generatedAt", LocalDateTime.now());
            report.put("format", format);
            
            return report;
            
        } catch (Exception e) {
            logger.error("Error al generar reporte de ventas", e);
            return Map.of("error", "Error al generar reporte");
        }
    }

    /**
     * Métodos auxiliares privados
     */
    private String generateOrderNumber() {
        // Formato: ORD-YYYYMMDD-XXXX (ej: ORD-20241201-0001)
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = String.format("%04d", new Random().nextInt(10000));
        return "ORD-" + datePrefix + "-" + randomSuffix;
    }

    private BigDecimal calculateSubtotal(CartSession cartSession) {
        return cartSession.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getShippingCost(String shippingMethod) {
        return switch (shippingMethod) {
            case "standard" -> BigDecimal.valueOf(15.99);
            case "express" -> BigDecimal.valueOf(29.99);
            case "overnight" -> BigDecimal.valueOf(49.99);
            default -> BigDecimal.valueOf(15.99);
        };
    }

    private boolean isValidOrderStatus(String status) {
        return Arrays.asList("PENDING", "PAID", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED").contains(status);
    }

    private void restoreInventory(String orderNumber) {
        try {
            List<Map<String, Object>> items = orderItems.get(orderNumber);
            if (items != null) {
                for (Map<String, Object> item : items) {
                    Long productId = (Long) item.get("productId");
                    Integer quantity = (Integer) item.get("quantity");
                    
                    // Restaurar stock del producto
                    // En implementación real, usar repository para actualizar base de datos
                    logger.info("Restaurando {} unidades del producto {} al inventario", quantity, productId);
                }
            }
        } catch (Exception e) {
            logger.error("Error al restaurar inventario para orden: {}", orderNumber, e);
        }
    }
}
