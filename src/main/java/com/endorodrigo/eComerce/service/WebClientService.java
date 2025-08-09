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

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ZGYzYzIwZS05N2ZhLTQyNDUtYmNmZS1mNTg4MWVjZmNiMzgiLCJqdGkiOiI3OGM2ODYyNDBhM2RhZDM1ODViNDEyOWJkYTZiM2QxY2NmZTZiNzE0ZDdmNmQxNTI5YzBmMWIzZTNiMTVhYzU5NjNiMmNlOWUwNDRjZGM0MyIsImlhdCI6MTc1NDc3ODUyNS40Nzk3MSwibmJmIjoxNzU0Nzc4NTI1LjQ3OTcxMywiZXhwIjoxNzU0NzgyMTI1LjQ2OTU0OCwic3ViIjoiMyIsInNjb3BlcyI6W119.UfHcUMmcXHRft5TnTWVPgIRvtGLflA2X23sco4SY9HW-Bly93UTOklU7OQ8_ClmLkroGJ1ZPiLQIrw6R4WvthKCbvzWFbnT3TZ7Fohrd8qbDVceIzj0bu9kp_yqrndUjT3sXcM7P2Rw-ZESW6qBKZmFzjFP6cFMUoVy67YuwUJHPyK7s0wBAGxB3hydtfG9w5SnbX5ncsk7SC01LyMIC0BWRyWy2Zr9Vy2HxfnUB6kdhCTh-94yUlhj0T01YiXyjBtvWb5bwrScSkoDVnwz1fVgm_19VRaG5_8ozrjvEnxDMQ_LHRQDwg63HxC2wDqrBbngzISblJD-5LnaK0UCi4jgcyWPRFZJPreJCbgtTV-Kn-_BVKYNGQ1Q1kN1OdKXxDUgN0klcvWouolU6jOf0W6H80i1K9T2HPjs_g2nTr5uzQJd9DGyb1HImeknXrEogAjDB8qPJ-XHt6anCs62V-ATx9tXJN2Ovw_iaXJys6LV4lvDqT-TsFu9iJfKTdqCtP1DprkAikmiJ1o2ci_AcF5qcfkLBx0jZImMfVJch9tGrd4c2SqCbNxwSJjuXhNVld6mBZO2kZQ9L4T_ZyFmoiuReaGey9S-sZhF1m-2W6tUsTcwI1Ek3fxUXt1Tus0j8Tx6aSHhX--AQqZ47fk959ys_WyQ7JaS1d47zJojKFMQ"; // coloca tu token real aquí
    private final Logger log = LoggerFactory.getLogger(WebClientService.class);

    private final RestClient restClient;

    public WebClientService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api-sandbox.factus.com.co").build();

    }


    public String createPost(Cart factus) {
        try {
            log.info("toke! {}", TOKEN);

            // Realizar la solicitud HTTP
            ResponseEntity<String> response = restClient.post()
                    .uri("/v1/bills/validate")
                    .header("Authorization", "Bearer " + TOKEN)
                    .contentType(APPLICATION_JSON)
                    .body(factus)  // Usamos bodyValue en lugar de body para enviar el cuerpo correctamente
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response status code: {}", response.getStatusCode());

            if (response.getBody() != null) {
                log.info("Response body: {}", response.getBody());
            } else {
                log.info("Response body is empty");
            }

            return response.getBody() != null ? "success" : "failure";
        } catch (HttpStatusCodeException e) {
            // Si la respuesta HTTP tiene un error, puedes capturarlo aquí
            log.error("Error response: {}", e.getResponseBodyAsString());
            return "failure";
        } catch (RestClientException e) {
            // Si ocurre un error en la ejecución de la solicitud
            log.error("Error in request: {}", e.getMessage());
            return "failure";
        } catch (Exception e) {
            // Captura cualquier otra excepción que pueda ocurrir
            log.error("Unexpected error: {}", e.getMessage());
            return "failure";
        }
    }
}
