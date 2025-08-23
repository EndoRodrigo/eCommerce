package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.*;
import com.endorodrigo.eComerce.service.CartService;
import com.endorodrigo.eComerce.service.CustomerService;
import com.endorodrigo.eComerce.service.ItemService;
import com.endorodrigo.eComerce.service.NotificationService;
import com.endorodrigo.eComerce.service.PaymentService;
import com.endorodrigo.eComerce.service.SalesService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para manejar ventas y checkout del sistema eCommerce
 * Incluye gestión de carrito, checkout y procesamiento de pagos
 */
@Controller
@RequestMapping("/sales")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class SalesController {

    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);
    
    private final CartService cartService;
    private final ItemService itemService;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final SalesService salesService;
    private final NotificationService notificationService;

    public SalesController(CartService cartService, ItemService itemService, 
                         CustomerService customerService, PaymentService paymentService,
                         SalesService salesService, NotificationService notificationService) {
        this.cartService = cartService;
        this.itemService = itemService;
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.salesService = salesService;
        this.notificationService = notificationService;
    }

    /**
     * Ver carrito de compras
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        try {
            String sessionId = session.getId();
            CartSession cartSession = cartService.getCartSession(sessionId);
            
            if (cartSession != null && !cartSession.getItems().isEmpty()) {
                List<CartItem> cartItems = cartSession.getItems();
                BigDecimal total = cartSession.getSubtotal();
                
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("total", total);
                model.addAttribute("itemCount", cartItems.size());
                
                // Calcular impuestos y descuentos
                BigDecimal tax = total.multiply(BigDecimal.valueOf(0.08)); // 8% IVA
                BigDecimal discount = BigDecimal.ZERO; // Sin descuentos por defecto
                BigDecimal finalTotal = total.add(tax).subtract(discount);
                
                model.addAttribute("subtotal", total);
                model.addAttribute("tax", tax);
                model.addAttribute("discount", discount);
                model.addAttribute("finalTotal", finalTotal);
                
            } else {
                model.addAttribute("cartItems", List.of());
                model.addAttribute("total", BigDecimal.ZERO);
                model.addAttribute("itemCount", 0);
                model.addAttribute("message", "Tu carrito está vacío");
            }
            
            return "sales/cart";
            
        } catch (Exception e) {
            logger.error("Error al cargar carrito", e);
            model.addAttribute("error", "Error al cargar el carrito");
            return "sales/cart";
        }
    }

    /**
     * Agregar producto al carrito
     */
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {
        
        try {
            String sessionId = session.getId();
            Optional<Item> product = itemService.findById(productId);
            
            if (product.isPresent()) {
                Item item = product.get();
                
                // Verificar stock disponible
                if (item.getQuantity() < quantity) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Stock insuficiente. Disponible: " + item.getQuantity()));
                }
                
                // Agregar al carrito
                CartItem cartItem = new CartItem();
                cartItem.setProductId(item.getId());
                cartItem.setProductName(item.getName());
                cartItem.setPrice(BigDecimal.valueOf(item.getPrice()));
                cartItem.setQuantity(quantity);
                cartItem.setProductCode(item.getCode_reference());
                cartItem.setDescription(item.getName());
                cartItem.setStockAvailable(item.getQuantity());
                
                cartService.addToCart(sessionId, cartItem);
                
                // Obtener conteo actualizado del carrito
                int itemCount = cartService.getCartItemCount(sessionId);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto agregado al carrito",
                    "itemCount", itemCount,
                    "productName", item.getName()
                ));
                
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Producto no encontrado"));
            }
            
        } catch (Exception e) {
            logger.error("Error al agregar producto al carrito", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Actualizar cantidad en carrito
     */
    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @RequestParam Long productId,
            @RequestParam int quantity,
            HttpSession session) {
        
        try {
            String sessionId = session.getId();
            
            if (quantity <= 0) {
                // Eliminar item si cantidad es 0 o menor
                cartService.removeFromCart(sessionId, productId);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto eliminado del carrito"
                ));
            }
            
            // Verificar stock disponible
            Optional<Item> product = itemService.findById(productId);
            if (product.isPresent() && product.get().getQuantity() < quantity) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Stock insuficiente. Disponible: " + product.get().getQuantity()));
            }
            
            // Actualizar cantidad
            cartService.updateCartItemQuantity(sessionId, productId, quantity);
            
            // Obtener total actualizado
            BigDecimal total = cartService.getCartTotal(sessionId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cantidad actualizada",
                "total", total
            ));
            
        } catch (Exception e) {
            logger.error("Error al actualizar carrito", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Eliminar producto del carrito
     */
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam Long productId,
            HttpSession session) {
        
        try {
            String sessionId = session.getId();
            cartService.removeFromCart(sessionId, productId);
            
            // Obtener total actualizado
            BigDecimal total = cartService.getCartTotal(sessionId);
            int itemCount = cartService.getCartItemCount(sessionId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Producto eliminado del carrito",
                "total", total,
                "itemCount", itemCount
            ));
            
        } catch (Exception e) {
            logger.error("Error al eliminar producto del carrito", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Limpiar carrito
     */
    @PostMapping("/cart/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        try {
            String sessionId = session.getId();
            cartService.clearCart(sessionId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Carrito limpiado exitosamente"
            ));
            
        } catch (Exception e) {
            logger.error("Error al limpiar carrito", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Proceso de checkout
     */
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        try {
            String sessionId = session.getId();
            CartSession cartSession = cartService.getCartSession(sessionId);
            
            if (cartSession == null || cartSession.getItems().isEmpty()) {
                return "redirect:/sales/cart";
            }
            
            // Calcular totales
            BigDecimal subtotal = cartService.getCartTotal(sessionId);
            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.08));
            BigDecimal shipping = BigDecimal.valueOf(15.99); // Costo de envío fijo
            BigDecimal finalTotal = subtotal.add(tax).add(shipping);
            
            model.addAttribute("cartItems", cartSession.getItems());
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("tax", tax);
            model.addAttribute("shipping", shipping);
            model.addAttribute("finalTotal", finalTotal);
            
            // Formulario de cliente
            model.addAttribute("customer", new Customer());
            
            // Opciones de envío
            model.addAttribute("shippingOptions", List.of(
                Map.of("id", "standard", "name", "Envío Estándar", "price", "15.99", "days", "3-5 días"),
                Map.of("id", "express", "name", "Envío Express", "price", "29.99", "days", "1-2 días"),
                Map.of("id", "overnight", "name", "Envío Nocturno", "price", "49.99", "days", "24 horas")
            ));
            
            // Métodos de pago
            model.addAttribute("paymentMethods", List.of(
                Map.of("id", "credit_card", "name", "Tarjeta de Crédito/Débito", "icon", "fas fa-credit-card"),
                Map.of("id", "paypal", "name", "PayPal", "icon", "fab fa-paypal"),
                Map.of("id", "bank_transfer", "name", "Transferencia Bancaria", "icon", "fas fa-university")
            ));
            
            return "sales/checkout";
            
        } catch (Exception e) {
            logger.error("Error al cargar checkout", e);
            model.addAttribute("error", "Error al cargar el checkout");
            return "sales/checkout";
        }
    }

    /**
     * Procesar orden
     */
    @PostMapping("/checkout/process")
    public String processOrder(
            @Valid @ModelAttribute("customer") Customer customer,
            BindingResult result,
            @RequestParam String shippingMethod,
            @RequestParam String paymentMethod,
            @RequestParam String cardNumber,
            @RequestParam String cardExpiry,
            @RequestParam String cardCvv,
            HttpSession session,
            Model model) {
        
        try {
            if (result.hasErrors()) {
                // Volver a checkout con errores
                return checkout(session, model);
            }
            
            String sessionId = session.getId();
            CartSession cartSession = cartService.getCartSession(sessionId);
            
            if (cartSession == null || cartSession.getItems().isEmpty()) {
                return "redirect:/sales/cart";
            }
            
            // Validar stock antes de procesar
            if (!validateStock(cartSession)) {
                model.addAttribute("error", "Algunos productos no tienen stock suficiente");
                return checkout(session, model);
            }
            
            // Crear o actualizar cliente
            Customer savedCustomer = customerService.insert(customer);
            
            // Crear orden
            String orderNumber = salesService.createOrder(cartSession, savedCustomer, shippingMethod);
            
            // Procesar pago
            Payment payment = new Payment();
            payment.setOrderNumber(orderNumber);
            payment.setAmount(cartService.getCartTotal(sessionId));
            payment.setMethod(paymentMethod);
            payment.setStatus("PENDING");
            payment.setCreatedAt(LocalDateTime.now());
            
            Payment savedPayment = paymentService.insert(payment);
            
            // Procesar pago con gateway (simulado)
            boolean paymentSuccess = processPaymentWithGateway(savedPayment, cardNumber, cardExpiry, cardCvv);
            
            if (paymentSuccess) {
                // Actualizar estado de pago
                savedPayment.setStatus("COMPLETED");
                paymentService.insert(savedPayment);
                
                // Actualizar estado de orden
                salesService.updateOrderStatus(orderNumber, "PAID");
                
                // Actualizar inventario
                updateInventory(cartSession);
                
                // Enviar notificaciones
                sendOrderNotifications(orderNumber, savedCustomer, cartSession);
                
                // Limpiar carrito
                cartService.clearCart(sessionId);
                
                // Redirigir a confirmación
                return "redirect:/sales/confirmation/" + orderNumber;
                
            } else {
                // Pago fallido
                savedPayment.setStatus("FAILED");
                paymentService.insert(savedPayment);
                
                model.addAttribute("error", "El pago no pudo ser procesado. Intente nuevamente.");
                return checkout(session, model);
            }
            
        } catch (Exception e) {
            logger.error("Error al procesar orden", e);
            model.addAttribute("error", "Error al procesar la orden. Intente nuevamente.");
            return checkout(session, model);
        }
    }

    /**
     * Confirmación de orden
     */
    @GetMapping("/confirmation/{orderNumber}")
    public String orderConfirmation(@PathVariable String orderNumber, Model model) {
        try {
            // Obtener detalles de la orden
            Map<String, Object> orderDetails = salesService.getOrderDetails(orderNumber);
            
            if (orderDetails == null) {
                model.addAttribute("error", "Orden no encontrada");
                return "sales/confirmation";
            }
            
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("orderNumber", orderNumber);
            
            return "sales/confirmation";
            
        } catch (Exception e) {
            logger.error("Error al cargar confirmación", e);
            model.addAttribute("error", "Error al cargar la confirmación");
            return "sales/confirmation";
        }
    }

    /**
     * Historial de órdenes
     */
    @GetMapping("/orders")
    public String orderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        try {
            // Obtener historial de órdenes del usuario actual
            // Implementar lógica para obtener órdenes del usuario autenticado
            
            model.addAttribute("orders", List.of()); // Placeholder
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", 1);
            
            return "sales/orders";
            
        } catch (Exception e) {
            logger.error("Error al cargar historial de órdenes", e);
            model.addAttribute("error", "Error al cargar el historial");
            return "sales/orders";
        }
    }

    /**
     * Detalles de orden específica
     */
    @GetMapping("/orders/{orderNumber}")
    public String orderDetails(@PathVariable String orderNumber, Model model) {
        try {
            Map<String, Object> orderDetails = salesService.getOrderDetails(orderNumber);
            
            if (orderDetails == null) {
                model.addAttribute("error", "Orden no encontrada");
                return "sales/order-details";
            }
            
            model.addAttribute("orderDetails", orderDetails);
            return "sales/order-details";
            
        } catch (Exception e) {
            logger.error("Error al cargar detalles de orden", e);
            model.addAttribute("error", "Error al cargar los detalles");
            return "sales/order-details";
        }
    }

    /**
     * Cancelar orden
     */
    @PostMapping("/orders/{orderNumber}/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable String orderNumber) {
        try {
            boolean cancelled = salesService.cancelOrder(orderNumber);
            
            if (cancelled) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Orden cancelada exitosamente"
                ));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "No se pudo cancelar la orden"));
            }
            
        } catch (Exception e) {
            logger.error("Error al cancelar orden", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * API para obtener total del carrito
     */
    @GetMapping("/api/cart/total")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartTotal(HttpSession session) {
        try {
            String sessionId = session.getId();
            BigDecimal total = cartService.getCartTotal(sessionId);
            int itemCount = cartService.getCartItemCount(sessionId);
            
            return ResponseEntity.ok(Map.of(
                "total", total,
                "itemCount", itemCount
            ));
            
        } catch (Exception e) {
            logger.error("Error al obtener total del carrito", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Métodos auxiliares privados
     */
    private boolean validateStock(CartSession cartSession) {
        /*for (Cart item : cartSession.getItems()) {
            Optional<Item> product = itemService.findById(item.getId());
            if (product.isPresent() && product.get().getStock() < item.getQuantity()) {
                return false;
            }
        }*/
        return true;
    }

    private boolean processPaymentWithGateway(Payment payment, String cardNumber, String cardExpiry, String cardCvv) {
        // Simular procesamiento de pago
        // En implementación real, integrar con gateway de pago (Stripe, PayPal, etc.)
        try {
            // Simular delay de procesamiento
            Thread.sleep(2000);
            
            // Simular éxito del pago (90% de éxito)
            return Math.random() > 0.1;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void updateInventory(CartSession cartSession) {
        /*for (Cart item : cartSession.getItems()) {
            Optional<Item> product = itemService.findById(item.getId());
            if (product.isPresent()) {
                Item updatedProduct = product.get();
                updatedProduct.setStock(updatedProduct.getStock() - item.getQuantity());
                itemService.saveItem(updatedProduct);
                
                // Enviar notificación de stock bajo si es necesario
                if (updatedProduct.getStock() <= updatedProduct.getMinStock()) {
                    notificationService.sendLowStockAlert(updatedProduct);
                }
            }
        }*/
    }

    private void sendOrderNotifications(String orderNumber, Customer customer, CartSession cartSession) {
        try {
            // Notificar al cliente
            Map<String, Object> orderData = Map.of(
                "orderNumber", orderNumber,
                "customerName", customer.getNames(),
                "total", cartService.getCartTotal(cartSession.getSessionId()),
                "items", cartSession.getItems()
            );
            
            notificationService.sendSaleNotification(null, orderData); // Placeholder para User
            
            // Notificar a administradores sobre venta de alto valor
            BigDecimal total = cartService.getCartTotal(cartSession.getSessionId());
            if (total.compareTo(BigDecimal.valueOf(1000)) > 0) {
                notificationService.sendSaleNotification(null, orderData); // Placeholder para User
            }
            
        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de orden", e);
        }
    }
}
