package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.LoginFactus;
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

    public LoginService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api-sandbox.factus.com.co").build();

    }


    public String login() {
        LoginFactus login = new LoginFactus();
        log.info("Datos body {}", login);

        JsonNode body = null;

        try {
            ResponseEntity<JsonNode> response = restClient.post()
                    .uri("/oauth/token")
                    .contentType(APPLICATION_JSON)
                    .body(login) // Aquí sí va body, pero luego se pasa directo a toEntity
                    .retrieve()
                    .toEntity(JsonNode.class);

            log.info("Request body {}", login);
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
