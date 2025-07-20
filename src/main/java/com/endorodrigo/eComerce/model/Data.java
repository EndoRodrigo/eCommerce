package com.endorodrigo.eComerce.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Data {

    private Integer id;
    @NotEmpty
    private String code;
    @NotNull
    private Integer client;

    public Data() {
    }

    public Data(Integer id, String code, Integer client) {
        this.id = id;
        this.code = code;
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", client='" + client + '\'' +
                '}';
    }
}
