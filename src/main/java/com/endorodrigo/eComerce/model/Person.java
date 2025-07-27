package com.endorodrigo.eComerce.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

@Entity
public class Person {
    @Id
    @Min(3)
    protected Integer id;
    @NotEmpty
    @Size(min = 3, max = 30)
    protected String name;
    @NotEmpty
    @Size(min = 3, max = 30)
    protected String lastName;
    @NotEmpty
    @Size(min = 3, max = 30)
    protected String email;
    @NotEmpty
    @Size(min = 3, max = 30)
    protected String phone;

    /**
     * Entidad base para personas en el sistema.
     * Incluye atributos comunes como nombre y email.
     */
    public Person() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
