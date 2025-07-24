package com.endorodrigo.eComerce.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un cliente en el sistema eCommerce.
 * Hereda de Person y añade dirección.
 */
@Entity
public class Customer extends Person {
    private String address;

    public Customer() {}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Customer{" +
                "address='" + address + '\'' +
                ", " + super.toString() +
                '}';
    }
}
