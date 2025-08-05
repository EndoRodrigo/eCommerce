package com.endorodrigo.eComerce.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa el carrito de compras de un usuario.
 * Contiene los ítems, el cliente y el pago asociado.
 */
@Component
@SessionScope  // Cada usuario tendrá su propio carrito
@Entity
public class Cart {

    private int numbering_range_id = 8;
    @Id
    private String reference_code;
    private String description;
    private String payment_method_code;

    @OneToOne
    private Customer customer;

    @OneToMany
    private List<Item> items = new ArrayList<>();

    public Cart() {
    }

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

    public void addProduct(Item product) {
        for (Item item : items) {
            if (item.getCode_reference().equals(product.getCode_reference())) {
                item.setQuantity(item.getQuantity() + product.getQuantity());
                return;
            }
        }
        Item newItem = new Item(product, 1);
        items.add(newItem);
    }

    public void removeProduct(Item product) {
        items.removeIf(item -> item.getCode_reference().equals(product.getCode_reference()));
    }

    public void updateProductQuantity(Item product, int quantity) {
        for (Item item : items) {
            if (item.getCode_reference().equals(product.getCode_reference())) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }


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
                ", items=" + items
                ;
    }
}
