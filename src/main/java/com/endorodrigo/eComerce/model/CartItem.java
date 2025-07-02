package com.endorodrigo.eComerce.model;

import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idItem;
    @OneToOne
    private Product product;
    private int quantity;
    @OneToOne
    private Payment payment;

    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.payment = getPayment();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return product.getPrice() * quantity;
    }

    public Payment getPayment() {
        payment = new Payment();
        payment.setIdPayment(1);
        payment.setDescription("Cash");
        payment.setPrice(getTotalPrice());
        return payment;
    }


    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", payment=" + payment +
                '}';
    }
}
