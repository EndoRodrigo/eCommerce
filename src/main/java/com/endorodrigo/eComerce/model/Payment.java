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
    private String description; // Descripción del pago
    private double price; // Monto
    private String method; // Método de pago (Efectivo, Tarjeta, Transferencia, etc.)
    private String status; // Estado del pago (Pagado, Pendiente, Cancelado)

    /**
     * Entidad que representa un pago realizado en el sistema.
     * Incluye información sobre el monto, método y estado del pago.
     */
    public Payment() {
    }

    public Payment(String description, double price, String method, String status) {
        this.description = description;
        this.price = price;
        this.method = method;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
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

    public void setMethod(String method) {
        this.method = method;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "idPayment=" + idPayment +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
