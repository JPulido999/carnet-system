package com.unsch.carnet_digital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionProxyController {

    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<?> getTransactions(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8091/api/transactions")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("page", page)
                .queryParam("size", size);

        if (dni != null && !dni.isBlank()) {
            builder.queryParam("dni", dni);
        }
        if (nombre != null && !nombre.isBlank()) {
            builder.queryParam("nombre", nombre);
        }
        if (departamento != null && !departamento.isBlank()) {
            builder.queryParam("departamento", departamento);
        }

        String url = builder.toUriString();

        return restTemplate.getForEntity(url, Object.class);
    }

    @GetMapping("/mis-accesos")
    public ResponseEntity<?> getMisAccesos(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8091/api/transactions/mis-accesos")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("dni", dni)
                .queryParam("page", page)
                .queryParam("size", size);

        String url = builder.toUriString();

        return restTemplate.getForEntity(url, Object.class);
    }

}
