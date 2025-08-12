package com.endorodrigo.eComerce.model;

public class LoginFactus {

    private String username = "sandbox@factus.com.co";
    private String password = "sandbox2024%";
    private String client_secret = "jiK7UVOiWd5nfQ9gwY5dgOoj9lgDPICqOcXnsQ1b";
    private String client_id = "9df3c20e-97fa-4245-bcfe-f5881ecfcb38";;
    private String grant_type = "password";
    private String token;
    private String refresh_token;

    public LoginFactus() {
    }

    public LoginFactus(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public String toString() {
        return "LoginFactus{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", client_id='" + client_id + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", token='" + token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}
