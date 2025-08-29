package com.endorodrigo.eCommerce.service;


import com.endorodrigo.eCommerce.exception.FacturaException;
import com.endorodrigo.eCommerce.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


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


    /**
     * Crea una factura enviando los datos del carrito a la API de Factus.
     * @param factus Carrito con los datos de la factura
     * @return "success" si se creó correctamente
     * @throws FacturaException Si hay un error en la comunicación o validación
     */
    public String createPost(Cart factus) {
        try {
            // Validaciones
            if (factus == null) {
                throw new FacturaException("Los datos de la factura no pueden ser nulos", "NULL_DATA");
            }
            
            if (factus.getItems() == null || factus.getItems().isEmpty()) {
                throw new FacturaException("La factura debe contener al menos un producto", "EMPTY_ITEMS");
            }
            
            if (factus.getCustomer() == null) {
                throw new FacturaException("La factura debe tener un cliente asociado", "NO_CUSTOMER");
            }
            
            log.info("Token: {}", TOKEN);

            ResponseEntity<String> response = restClient.post()
                    .uri("/v1/bills/validate")
                    .header("Authorization", "Bearer " + obtenerToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(factus)
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return "success";
            } else {
                throw new FacturaException("Error en la respuesta de la API: " + response.getStatusCode(), "API_ERROR");
            }

        } catch (HttpStatusCodeException e) {
            log.error("Error response: {}", e.getResponseBodyAsString());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getResponseBodyAsString(), "HTTP_ERROR", e);
        } catch (RestClientException e) {
            log.error("Error in request: {}", e.getMessage());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getMessage(), "REST_CLIENT_ERROR", e);
        } catch (FacturaException e) {
            throw e; // Re-lanzar excepciones de facturación
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new FacturaException("Error inesperado en la facturación: " + e.getMessage(), "UNEXPECTED_ERROR", e);
        }
    }

    /**
     * Obtiene la lista de facturas desde la API de Factus.
     * @return Lista de facturas en formato JSON
     * @throws FacturaException Si hay un error en la comunicación
     */
    public String getPost(){
        try{
            log.info("Token: {}", TOKEN);

            ResponseEntity<String> response = restClient.get()
                    .uri("/v1/bills")
                    .header("Authorization", "Bearer " + obtenerToken())
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new FacturaException("Error al obtener las facturas: " + response.getStatusCode(), "API_ERROR");
            }
        } catch (HttpStatusCodeException e) {
            log.error("Error response: {}", e.getResponseBodyAsString());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getResponseBodyAsString(), "HTTP_ERROR", e);
        } catch (RestClientException e) {
            log.error("Error in request: {}", e.getMessage());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getMessage(), "REST_CLIENT_ERROR", e);
        } catch (FacturaException e) {
            throw e; // Re-lanzar excepciones de facturación
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new FacturaException("Error inesperado al obtener las facturas: " + e.getMessage(), "UNEXPECTED_ERROR", e);
        }
    }

    /**
     * Obtiene el detalle de una factura específica desde la API de Factus.
     * @param number Número de la factura
     * @return Detalle de la factura en formato JSON
     * @throws FacturaException Si hay un error en la comunicación
     */
    public String getDetailPost(String number){
        try{
            // Validaciones
            if (number == null || number.trim().isEmpty()) {
                throw new FacturaException("El número de factura no puede ser nulo o vacío", "INVALID_NUMBER");
            }
            
            log.info("Numero de factura: {}", number);

            ResponseEntity<String> response = restClient.get()
                    .uri("/v1/bills/show/{number}",number)
                    .header("Authorization", "Bearer " + obtenerToken())
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new FacturaException("Error al obtener el detalle de la factura: " + response.getStatusCode(), "API_ERROR");
            }
        } catch (HttpStatusCodeException e) {
            log.error("Error response: {}", e.getResponseBodyAsString());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getResponseBodyAsString(), "HTTP_ERROR", e);
        } catch (RestClientException e) {
            log.error("Error in request: {}", e.getMessage());
            throw new FacturaException("Error en la comunicación con la API de facturación: " + e.getMessage(), "REST_CLIENT_ERROR", e);
        } catch (FacturaException e) {
            throw e; // Re-lanzar excepciones de facturación
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new FacturaException("Error inesperado al obtener el detalle de la factura: " + e.getMessage(), "UNEXPECTED_ERROR", e);
        }
    }

}
