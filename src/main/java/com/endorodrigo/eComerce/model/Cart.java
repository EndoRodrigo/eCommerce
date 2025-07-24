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

    @Id
    private Long idCar;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Payment payment;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Constructor vacío requerido por JPA
    public Cart() {
    }

    // Getters y setters
    public Long getIdCar() {
        return idCar;
    }

    public void setIdCar(Long idCar) {
        this.idCar = idCar;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.addQuantity(1);
                return;
            }
        }
        CartItem newItem = new CartItem(product, 1);
        newItem.setCart(this);
        items.add(newItem);
    }

    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().getId().equals(product.getId()));
    }

    public void updateProductQuantity(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
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
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }



    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void clear() {
        items.clear();
    }


    @Override
    public String toString() {
        return "Cart{" +
                "idCar=" + idCar +
                ", items=" + items +
                ", customer=" + customer +
                ", payment=" + payment +
                '}';
    }
}
