package com.endorodrigo.eComerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    private static Logger LOG = LoggerFactory.getLogger(Cart.class);

    @Id
    @Column(name = "codeReferenceCode")
    private String reference_code;

    private String description = "Factura de Venta";

    private String payment_method_code;

    private int numbering_range_id = 8;


    @ManyToOne
    @JoinColumn(name = "identification") // este es el foreign key en la tabla 'cart'
    private Customer customer;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public Cart() {}

    // Getters y Setters

    public int getNumbering_range_id() {
        return numbering_range_id;
    }

    public void setNumbering_range_id(int numbering_range_id) {
        this.numbering_range_id = numbering_range_id;
    }

    public String getReference_code() {
        return reference_code;
    }

    public void setReference_code(String reference_code) {
        this.reference_code = reference_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_method_code() {
        return payment_method_code;
    }

    public void setPayment_method_code(String payment_method_code) {
        this.payment_method_code = payment_method_code;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Métodos de negocio
    @JsonIgnore
    public void addProduct(Item product) {
        items.add(product);
        product.setCart(this);
    }
    @JsonIgnore
    public void removeProduct(Item product) {
        items.removeIf(item -> item.getCode_reference().equals(product.getCode_reference()));
    }

    @JsonIgnore
    public void updateProductQuantity(Item product, int quantity) {
        for (Item item : items) {
            if (item.getCode_reference().equals(product.getCode_reference())) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
    @JsonIgnore
    public boolean isEmpty() {
        return items.isEmpty();
    }
    @JsonIgnore
    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    @JsonIgnore
    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "numbering_range_id=" + numbering_range_id +
                ", reference_code='" + reference_code + '\'' +
                ", description='" + description + '\'' +
                ", payment_method_code='" + payment_method_code + '\'' +
                ", customer=" + customer +
                ", items=" + items +
                '}';
    }
}
