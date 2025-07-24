package com.endorodrigo.eComerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int idPayment;
    private String description;
    private double price;

/**
 * Entidad que representa un pago realizado en el sistema.
 * Incluye información sobre el monto, método y estado del pago.
 */
    public Payment() {
    }

    public Payment(String description, double price) {
        this.description = description;
        this.price = price;
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
