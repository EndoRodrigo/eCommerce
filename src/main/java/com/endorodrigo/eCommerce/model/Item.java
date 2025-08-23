package com.endorodrigo.eComerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty
    private String code_reference;
    
    @NotEmpty
    private String name;
    
    @NotNull
    private Integer quantity;
    
    @NotEmpty
    private String discount_rate;
    
    @NotNull
    private float price;
    
    @NotEmpty
    private String tax_rate;
    
    @NotNull
    private Integer unit_measure_id;
    
    @NotNull
    private Integer standard_code_id;
    
    @NotNull
    private Integer is_excluded;
    
    @NotNull
    private Integer tribute_id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode_reference() {
        return code_reference;
    }

    public void setCode_reference(String code_reference) {
        this.code_reference = code_reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public Integer getUnit_measure_id() {
        return unit_measure_id;
    }

    public void setUnit_measure_id(Integer unit_measure_id) {
        this.unit_measure_id = unit_measure_id;
    }

    public Integer getStandard_code_id() {
        return standard_code_id;
    }

    public void setStandard_code_id(Integer standard_code_id) {
        this.standard_code_id = standard_code_id;
    }

    public Integer getIs_excluded() {
        return is_excluded;
    }

    public void setIs_excluded(Integer is_excluded) {
        this.is_excluded = is_excluded;
    }

    public Integer getTribute_id() {
        return tribute_id;
    }

    public void setTribute_id(Integer tribute_id) {
        this.tribute_id = tribute_id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", code_reference='" + code_reference + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", discount_rate='" + discount_rate + '\'' +
                ", price=" + price +
                ", tax_rate='" + tax_rate + '\'' +
                ", unit_measure_id=" + unit_measure_id +
                ", standard_code_id=" + standard_code_id +
                ", is_excluded=" + is_excluded +
                ", tribute_id=" + tribute_id +
                ", cart=" + cart +
                '}';
    }
}
