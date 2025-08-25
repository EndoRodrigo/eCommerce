package com.endorodrigo.eCommerce.service;


import com.endorodrigo.eCommerce.model.LoginFactus;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class LoginService {
    private final Logger log = LoggerFactory.getLogger(LoginService.class);
    public static String TOKEN = null;
    private final RestClient restClient;
    private final LoginFactus loginFactus;

    public LoginService(LoginFactus loginFactus, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api-sandbox.factus.com.co").build();
        this.loginFactus = loginFactus;
    }


    public String login() {
        log.info("Datos body {}", loginFactus);

        JsonNode body = null;

        try {
            ResponseEntity<JsonNode> response = restClient.post()
                    .uri("/oauth/token")
                    .contentType(APPLICATION_JSON)
                    .body(loginFactus) // Aquí sí va body, pero luego se pasa directo a toEntity
                    .retrieve()
                    .toEntity(JsonNode.class);

            log.info("Request body {}", loginFactus);
            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (response.getBody() != null) {
                body = response.getBody();
                TOKEN = body.get("access_token").asText();
            } else {
                log.info("Response body is empty");
            }
        } catch (Exception e) {
            log.error("Error al hacer login: ", e);
        }

        return TOKEN;
    }

}
