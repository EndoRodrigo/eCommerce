package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class WebClientService {

    private static String TOKEN = ""; // coloca tu token real aquí
    private final Logger log = LoggerFactory.getLogger(WebClientService.class);


    private final LoginService loginService;

    private final RestClient restClient;

    public WebClientService(LoginService loginService, RestClient.Builder restClientBuilder) {
        this.loginService = loginService;
        this.restClient = restClientBuilder.baseUrl("https://api-sandbox.factus.com.co").build();

    }

    public String obtenerToken() {
        TOKEN = loginService.login();
        return TOKEN;
    }


    public String createPost(Cart factus) {
        try {
            log.info("Token: {}", TOKEN);

            ResponseEntity<String> response = restClient.post()
                    .uri("/v1/bills/validate")
                    .header("Authorization", "Bearer " + TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(factus) // ✅ Enviar el objeto como JSON
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            return (response.getBody() != null) ? "success" : "failure";

        } catch (HttpStatusCodeException e) {
            log.error("Error response: {}", e.getResponseBodyAsString());
            return "failure";
        } catch (RestClientException e) {
            log.error("Error in request: {}", e.getMessage());
            return "failure";
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return "failure";
        }
    }

}
