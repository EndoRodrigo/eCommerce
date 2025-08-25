package com.endorodrigo.eCommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una sesión de carrito de compras
 * Contiene los items del carrito y metadatos de la sesión
 */
public class CartSession {
    
    private String sessionId;
    private List<CartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String discountCode;
    private BigDecimal discountAmount;
    private String customerId;
    private String customerEmail;
    private String customerName;
    private String notes;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String currency;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private String status; // "active", "abandoned", "converted", "expired"

    public CartSession() {
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.discountAmount = BigDecimal.ZERO;
        this.shippingCost = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.currency = "USD";
        this.status = "active";
    }

    // Getters y Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Métodos de negocio
    public void addItem(CartItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(CartItem item) {
        if (this.items != null) {
            this.items.remove(item);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removeItemById(Long productId) {
        if (this.items != null) {
            this.items.removeIf(item -> productId.equals(item.getProductId()));
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void clearItems() {
        if (this.items != null) {
            this.items.clear();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    public int getItemCount() {
        return this.items != null ? this.items.size() : 0;
    }

    public int getTotalQuantity() {
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        }
        return this.items.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }

    public BigDecimal getSubtotal() {
        if (this.items == null || this.items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return this.items.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotal() {
        BigDecimal subtotal = getSubtotal();
        BigDecimal total = subtotal;
        
        if (this.shippingCost != null) {
            total = total.add(this.shippingCost);
        }
        
        if (this.taxAmount != null) {
            total = total.add(this.taxAmount);
        }
        
        if (this.discountAmount != null && this.discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            total = total.subtract(this.discountAmount);
        }
        
        return total.max(BigDecimal.ZERO);
    }

    public void applyDiscount(String code, BigDecimal amount) {
        this.discountCode = code;
        this.discountAmount = amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void removeDiscount() {
        this.discountCode = null;
        this.discountAmount = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        if (this.updatedAt == null) {
            return true;
        }
        // Considerar expirada si no se ha actualizado en las últimas 24 horas
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        return this.updatedAt.isBefore(expirationTime);
    }

    public void refresh() {
        this.updatedAt = LocalDateTime.now();
    }

    public CartItem findItemById(Long productId) {
        if (this.items == null || this.items.isEmpty()) {
            return null;
        }
        return this.items.stream()
            .filter(item -> productId.equals(item.getProductId()))
            .findFirst()
            .orElse(null);
    }

    public boolean containsProduct(Long productId) {
        return findItemById(productId) != null;
    }

    public void updateItemQuantity(Long productId, int newQuantity) {
        CartItem item = findItemById(productId);
        if (item != null) {
            item.setQuantity(newQuantity);
            this.updatedAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "CartSession{" +
                "sessionId='" + sessionId + '\'' +
                ", items=" + items +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", discountCode='" + discountCode + '\'' +
                ", discountAmount=" + discountAmount +
                ", customerId='" + customerId + '\'' +
                ", status='" + status + '\'' +
                ", itemCount=" + getItemCount() +
                ", totalQuantity=" + getTotalQuantity() +
                ", total=" + getTotal() +
                '}';
    }
}
