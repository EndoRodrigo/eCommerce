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

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ZGYzYzIwZS05N2ZhLTQyNDUtYmNmZS1mNTg4MWVjZmNiMzgiLCJqdGkiOiI3M2IyMzZjY2ExMTEwMGEzMDcwYWY1MjUxOWFlYTA3YWM5OTAwOTQ5NTE4NjNmZmE4ZjZhZjA1ZGQ0NmViNjBjNGQ5OWEzNzYwZTViMGFmOSIsImlhdCI6MTc1NDc4MjMwMC43MjE2OTQsIm5iZiI6MTc1NDc4MjMwMC43MjE2OTYsImV4cCI6MTc1NDc4NTkwMC43MDkwNDIsInN1YiI6IjMiLCJzY29wZXMiOltdfQ.m796vlHKGNuSvhme41ck07efYPFs3q-RmhEWc_gQ01nPrHMKdEnnDi2e4fsYaiZM7LUWx6ngsHlvjXv5KUddWyVUE2BytZfyMtoct-pvxgDDVdb-b-wA6Ok4LPbJoGkfTNBa5kZjfxInbp1icrD2K6GhmugfNXME-PCwoLLiJ8BfOb2EkXmdW5S3jOLYbrtzcibSAxxFrY0pzCF83Hyd-2vTDrCDFlCdUyTiI9PZMh8b4n1QzAJjWndtaFT-lRM-JeR6d92UacOdU7mpPFxVgGrbEM6ldzhwA5aVJSisal6YxK-j7_sYS4lW4jNBSLbKl9nQIEuQnulpUYie41LEXlK7oQuKDISUC-67EGIh70z9PnpISeA_dtQXfzeE3s2Wix4tot9M89qCWihnzSMcUtFCN24D7HfIVCWCK92M7rgLXwDGC0pEEk9TSfz7tIDghgS-lJ4y8BR7DmODQqyxa7Sj3OFEARwijaAqKy2fjJNOI6TC9BNXRLJQYNykLYDXZbspTCzON2I_y7kEdFMyyL5v-6xyUv47zX3KhSHqFOqGhhrN9BwOg0sLjH3x5LjHR-zFjAiMwMt9YL_XXz6y969JQTar3cK4d7ScUKE_hrocjKYRcBpmN1dWKX7pY939l_CHIKhqw7DUfKminJT2L7IRR3wkB7Ptvj2VBGqwAcQ"; // coloca tu token real aquí
    private final Logger log = LoggerFactory.getLogger(WebClientService.class);

    private final RestClient restClient;

    public WebClientService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api-sandbox.factus.com.co").build();

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
