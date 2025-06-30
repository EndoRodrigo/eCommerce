package com.endorodrigo.eComerce.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
@SessionScope  // Cada usuario tendrá su propio carrito
public class Cart {

    Logger logger = Logger.getLogger(String.valueOf(Cart.class));

    public Cart() {
    }

    private int idCar;
    private List<CartItem> items = new ArrayList<>();

    public int getIdCar() {
        return idCar;
    }

    public void setIdCar(int idCar) {
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
        logger.info("addProduct add list. "+ product);
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