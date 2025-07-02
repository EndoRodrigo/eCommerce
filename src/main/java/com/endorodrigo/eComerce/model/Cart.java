package com.endorodrigo.eComerce.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
@SessionScope  // Cada usuario tendrá su propio carrito
@Entity
public class Cart {


    public Cart() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCar;

    @OneToMany
    private List<CartItem> items = new ArrayList<>();

    public Integer getIdCar() {
        return idCar;
    }

    public void setIdCar(Integer idCar) {
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
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idCar=" + idCar +
                ", items=" + items +
                '}';
    }
}