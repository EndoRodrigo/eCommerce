package com.endorodrigo.eComerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private int idPayment;
    private String description;
    private double price;

    public Payment() {
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "idPayment=" + idPayment +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
