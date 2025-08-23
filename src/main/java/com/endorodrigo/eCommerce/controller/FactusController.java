package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.service.WebClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class FactusController {

    private final static Logger log = LoggerFactory.getLogger(FactusController.class);

    private final WebClientService webClientService;

    public FactusController(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @GetMapping("/factus")
    public String factus(Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(webClientService.getPost());
        List factus = mapper.convertValue(
                root.path("data").path("data"),
                List.class
        );

        model.addAttribute("bill",factus);

        return "factus";
    }

    @GetMapping("/detalle/{id}")
    public String getDetail(@PathVariable String id, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(webClientService.getDetailPost(id));
        Map<String, Object> data = mapper.convertValue(
                root.path("data"),
                new TypeReference<Map<String, Object>>() {}
        );
        log.info("Informacion de factura {}",data);
        model.addAttribute("bill",data);

        return "detalle";
    }
}
