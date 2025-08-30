package com.endorodrigo.eCommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeReferenceCode")
    private String reference_code;

    private String description = "Factura de Venta";

    private String payment_method_code;

    private int numbering_range_id = 8;

    @ManyToOne
    @JoinColumn(name = "identification") // este es el foreign key en la tabla 'cart'
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();

    public Cart() {}

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    // Métodos de negocio

    @JsonIgnore
    public void addProduct(Item product) {
        product.setCart(this);  // Asociar el cart con el item
        items.add(product);  // Agregar el producto a la lista de items
    }

    @JsonIgnore
    public void removeProduct(Item product) {
        items.removeIf(item -> item.getCode_reference().equals(product.getCode_reference()));  // Remover item de la lista
    }

    @JsonIgnore
    public void updateProductQuantity(Item product, int quantity) {
        for (Item item : items) {
            if (item.getCode_reference().equals(product.getCode_reference())) {
                item.setQuantity(quantity);  // Actualizar la cantidad del producto
                return;
            }
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return items.isEmpty();  // Verificar si el carrito está vacío
    }

    @JsonIgnore
    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())  // Calcular el total
                .sum();
    }

    @JsonIgnore
    public void clear() {
        items.clear();  // Limpiar todos los productos
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", numbering_range_id=" + numbering_range_id +
                ", reference_code='" + reference_code + '\'' +
                ", description='" + description + '\'' +
                ", payment_method_code='" + payment_method_code + '\'' +
                ", customer=" + customer +
                ", items=" + items +
                '}';
    }
}
