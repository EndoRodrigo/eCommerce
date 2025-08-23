package com.endorodrigo.eComerce.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad que representa datos generales asociados a un cliente o transacción.
 * Incluye identificador, código y referencia al cliente.
 */
public class Data {

    /** Identificador único de los datos. */
    private Integer id;
    /** Código asociado a los datos. */
    @NotEmpty
    private String code;
    /** Identificador del cliente relacionado. */
    @NotNull
    private String client;
    /** Método de pago seleccionado */
    private String paymentMethod;

    /**
     * Constructor por defecto.
     */
    public Data() {
    }

    /**
     * Constructor con todos los atributos.
     * @param id Identificador único
     * @param code Código asociado
     * @param client Identificador del cliente
     */
    public Data(Integer id, String code, String client) {
        this.id = id;
        this.code = code;
        this.client = client;
    }

    /**
     * Obtiene el identificador único.
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador único.
     * @param id Identificador
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el código asociado.
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código asociado.
     * @param code Código
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene el identificador del cliente relacionado.
     * @return client
     */
    public String getClient() {
        return client;
    }

    /**
     * Establece el identificador del cliente relacionado.
     * @param client Identificador del cliente
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * Obtiene el método de pago seleccionado.
     * @return paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Establece el método de pago seleccionado.
     * @param paymentMethod Método de pago
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Representación en texto de la entidad Data.
     * @return Cadena con los valores de los atributos
     */
    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", client='" + client + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
