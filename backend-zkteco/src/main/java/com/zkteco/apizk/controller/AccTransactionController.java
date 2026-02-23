package com.zkteco.apizk.controller;


import com.zkteco.apizk.service.AccTransactionService;
import org.springframework.web.bind.annotation.*;

import com.zkteco.apizk.dto.AccTransactionDTO;
import com.zkteco.apizk.dto.AccesosPorDispositivoDTO;
import com.zkteco.apizk.dto.AccesosPorEventoDTO;
import com.zkteco.apizk.dto.AccesosPorHoraDTO;
import com.zkteco.apizk.entity.AccTransaction;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class AccTransactionController {

    private final AccTransactionService service;

    public AccTransactionController(AccTransactionService service) {
        this.service = service;
    }

    /*@GetMapping
    public Page<AccTransaction> buscar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return service.buscar(startTime, endTime, page, size);
    }*/

    /*segunda verion del filtro basico de busqueda
    @GetMapping
    public Page<AccTransactionDTO> getTransactions(
            @RequestParam LocalDateTime  startDate,
            @RequestParam LocalDateTime  endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.getTransactions(startDate, endDate, page, size);
    }*/

    @GetMapping
    public Page<AccTransactionDTO> searchTransactions(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return service.search(
                startDate,
                endDate,
                dni,
                nombre,
                departamento,
                page,
                size
        );
    }

    @GetMapping("/mis-accesos")
    public Page<AccTransactionDTO> getMisAccesos(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam String dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.search(
                startDate,
                endDate,
                dni,      // 👈 solo filtra por DNI
                null,     // nombre
                null,     // departamento
                page,
                size
        );
    }


    @GetMapping("/total-filtrado")
    public Long getTotalFiltrado(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento
    ) {
        return service.getTotalFiltrado(startDate, endDate, dni, nombre, departamento);
    }


    @GetMapping("/por-dispositivo")
    public List<AccesosPorDispositivoDTO> getAccesosPorDispositivo(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        return service.getAccesosPorDispositivo(startDate, endDate);
    }

    @GetMapping("/total")
    public Long getTotalAccesos(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        return service.getTotalAccesos(startDate, endDate);
    }

    @GetMapping("/por-evento")
    public List<AccesosPorEventoDTO> getAccesosPorEvento(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        return service.getAccesosPorEvento(startDate, endDate);
    }

    @GetMapping("/por-hora")
    public List<AccesosPorHoraDTO> getAccesosPorHora(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return service.getAccesosPorHora(startDate, endDate);
    }
    
    @GetMapping("/ultimos")
    public List<AccTransactionDTO> getUltimosAccesos(
            @RequestParam(defaultValue = "50") int limit
    ) {
        return service.getUltimosAccesos(limit);
    }

    @GetMapping("/filtros/dispositivos")
    public List<String> getDispositivos() {
        return service.getDispositivos();
    }

    @GetMapping("/filtros/departamentos")
    public List<String> getDepartamentos() {
        return service.getDepartamentos();
    }


}

