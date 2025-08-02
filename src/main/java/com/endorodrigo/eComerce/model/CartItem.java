package com.endorodrigo.eComerce.model;

import jakarta.persistence.*;

/**
 * Entidad que representa un ítem dentro del carrito de compras.
 * Relaciona un producto y su cantidad en el carrito.
 */
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idItem;

    @ManyToOne
    @JoinColumn(name = "cart_id") // FK hacia la tabla Cart
    private Cart cart;

    @OneToOne
    private Items product;

    private int quantity;


    public CartItem() {}
    public CartItem(Items product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public Integer getIdItem() { return idItem; }
    public void setIdItem(Integer idItem) { this.idItem = idItem; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Items getProduct() { return product; }
    public void setProduct(Items product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    @Override
    public String toString() {
        return "CartItem{" +
                "idItem=" + idItem +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public double getTotalPrice() {
        return product != null ? product.getPrice() * quantity : 0;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }
}
