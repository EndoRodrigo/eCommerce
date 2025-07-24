package com.endorodrigo.eComerce.model;

public class Employed extends Person {
    /**
     * Entidad que representa un empleado del sistema.
     * Incluye información personal y laboral.
     */
    private String access;

    public Employed() {
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "Employed{" +
                "access='" + access + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
