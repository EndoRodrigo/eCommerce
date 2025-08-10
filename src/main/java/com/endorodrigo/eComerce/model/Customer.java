package com.endorodrigo.eComerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad que representa un cliente en el sistema eCommerce.
 * Hereda de Person y añade dirección.
 */
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "{NotNull.customer.identification_document_id}")
    private Integer identification_document_id;
    @NotEmpty(message = "{NotEmpty.customer.identification}")
    @Column(nullable = false)
    private String identification;
    @NotEmpty(message = "{NotEmpty.customer.names}")
    private String names;
    @NotEmpty(message = "{NotEmpty.customer.address}")
    private String address;
    @NotEmpty(message = "{NotEmpty.customer.legal_organization_id}")
    private String legal_organization_id;
    @NotEmpty(message = "{NotEmpty.customer.tribute_id}")
    private String tribute_id;
    @NotEmpty(message = "{NotEmpty.customer.municipality_id}")
    private String municipality_id;


    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegal_organization_id() {
        return legal_organization_id;
    }

    public void setLegal_organization_id(String legal_organization_id) {
        this.legal_organization_id = legal_organization_id;
    }

    public String getTribute_id() {
        return tribute_id;
    }

    public void setTribute_id(String tribute_id) {
        this.tribute_id = tribute_id;
    }

    public Integer getIdentification_document_id() {
        return identification_document_id;
    }

    public void setIdentification_document_id(Integer identification_document_id) {
        this.identification_document_id = identification_document_id;
    }

    public String getMunicipality_id() {
        return municipality_id;
    }

    public void setMunicipality_id(String municipality_id) {
        this.municipality_id = municipality_id;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", identification_document_id=" + identification_document_id +
                ", identification='" + identification + '\'' +
                ", names='" + names + '\'' +
                ", address='" + address + '\'' +
                ", legal_organization_id='" + legal_organization_id + '\'' +
                ", tribute_id='" + tribute_id + '\'' +
                ", municipality_id='" + municipality_id + '\'' +
                '}';
    }
}
