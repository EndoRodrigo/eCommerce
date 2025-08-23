package com.endorodrigo.eComerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase que representa un item individual en el carrito de compras
 * Contiene la información del producto y la cantidad seleccionada
 */
public class CartItem {
    
    private Long productId;
    private String productName;
    private String productCode;
    private String description;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
    private String image;
    private String category;
    private String brand;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private boolean available;
    private int stockAvailable;
    private String unitMeasure;
    private BigDecimal taxRate;
    private BigDecimal discountRate;
    private String notes;

    public CartItem() {
        this.addedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.available = true;
        this.taxRate = BigDecimal.ZERO;
        this.discountRate = BigDecimal.ZERO;
    }

    public CartItem(Long productId, String productName, BigDecimal price, int quantity) {
        this();
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters y Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        updateSubtotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Métodos de negocio
    private void updateSubtotal() {
        if (this.price != null && this.quantity > 0) {
            this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void incrementQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
        updateSubtotal();
    }

    public void decrementQuantity(int quantityToRemove) {
        if (this.quantity >= quantityToRemove) {
            this.quantity -= quantityToRemove;
            updateSubtotal();
        }
    }

    public BigDecimal getTaxAmount() {
        if (this.subtotal != null && this.taxRate != null) {
            return this.subtotal.multiply(this.taxRate).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getDiscountAmount() {
        if (this.subtotal != null && this.discountRate != null) {
            return this.subtotal.multiply(this.discountRate).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getFinalPrice() {
        BigDecimal finalPrice = this.subtotal;
        
        if (this.taxRate != null && this.taxRate.compareTo(BigDecimal.ZERO) > 0) {
            finalPrice = finalPrice.add(getTaxAmount());
        }
        
        if (this.discountRate != null && this.discountRate.compareTo(BigDecimal.ZERO) > 0) {
            finalPrice = finalPrice.subtract(getDiscountAmount());
        }
        
        return finalPrice.max(BigDecimal.ZERO);
    }

    public boolean hasStock() {
        return this.stockAvailable >= this.quantity;
    }

    public int getRemainingStock() {
        return Math.max(0, this.stockAvailable - this.quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return productId != null && productId.equals(cartItem.getProductId());
    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", available=" + available +
                ", stockAvailable=" + stockAvailable +
                '}';
    }
}
