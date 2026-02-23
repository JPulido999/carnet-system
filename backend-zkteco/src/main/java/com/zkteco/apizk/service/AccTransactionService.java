package com.zkteco.apizk.service;

import com.zkteco.apizk.dto.AccTransactionDTO;
import com.zkteco.apizk.dto.AccesosPorDispositivoDTO;
import com.zkteco.apizk.dto.AccesosPorEventoDTO;
import com.zkteco.apizk.dto.AccesosPorHoraDTO;
import com.zkteco.apizk.entity.AccTransaction;
import com.zkteco.apizk.util.EventDictionary;


import com.zkteco.apizk.repository.AccTransactionRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)
public class AccTransactionService {

    private final AccTransactionRepository repository;

    public AccTransactionService(AccTransactionRepository repository) {
        this.repository = repository;
    }

    public Page<AccTransaction> buscar(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int page,
        int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (startTime != null && endTime != null) {
            return repository.findByEventTimeBetweenOrderByEventTimeDesc(
                    startTime, endTime, pageable
            );
        }

        if (startTime != null) {
            return repository.findByEventTimeGreaterThanEqualOrderByEventTimeDesc(
                    startTime, pageable
            );
        }

        if (endTime != null) {
            return repository.findByEventTimeLessThanEqualOrderByEventTimeDesc(
                    endTime, pageable
            );
        }

        return repository.findAllByOrderByEventTimeDesc(pageable);
    }

   
    
    public Page<AccTransactionDTO> getTransactions(
            LocalDateTime  startDate,
            LocalDateTime  endDate,
            int page,
            int size
    ) {

        // límite defensivo (seguridad)
        if (size > 100) {
            size = 100;
        }

        PageRequest pageable = PageRequest.of(page, size);

        Page<AccTransactionDTO> pageResult = repository.findTransactionsByDateRange(
                startDate,
                endDate,
                pageable
        );

        return pageResult.map(this::decorate);
    }


    public List<AccesosPorDispositivoDTO> getAccesosPorDispositivo(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        List<Object[]> rows = repository.countAccesosRaw(startDate, endDate);

        return rows.stream()
                .map(r -> new AccesosPorDispositivoDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }

    public Long getTotalAccesos(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.countTotalAccesos(startDate, endDate);
    }


    public List<AccesosPorEventoDTO> getAccesosPorEvento(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return repository.countByEventName(startDate, endDate)
                .stream()
                .map(r -> new AccesosPorEventoDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }

    public List<AccesosPorHoraDTO> getAccesosPorHora(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return repository.countByHour(startDate, endDate)
                .stream()
                .map(r -> new AccesosPorHoraDTO(
                        ((Number) r[0]).intValue(),
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }

    public List<AccTransactionDTO> getUltimosAccesos(int limit) {
        if (limit > 100) limit = 100;
        return repository.findLastTransactions(limit)
            .stream()
            .map(this::decorate)
            .toList();
    }

    public List<String> getDispositivos() {
        return repository.findDistinctDevices();
    }

    public List<String> getDepartamentos() {
        return repository.findDepartments();
    }


    /*SERVICO POOTENTE DE FILTROS */
    public Page<AccTransactionDTO> search(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String dni,
            String nombre,
            String departamento,
            int page,
            int size
    ) {
        if (size > 100) size = 100;

        PageRequest pageable = PageRequest.of(page, size);

        // normalizar vacíos
        if (dni != null && dni.isBlank()) dni = null;
        if (nombre != null && nombre.isBlank()) nombre = null;
        if (departamento != null && departamento.isBlank()) departamento = null;

        Page<AccTransactionDTO> pageResult = repository.findTransactionsFiltrado(
                startDate,
                endDate,
                dni,
                nombre,
                departamento,
                pageable
        );

        return pageResult.map(this::decorate);
    }

    public Long getTotalFiltrado(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String dni,
            String nombre,
            String departamento
    ) {
        return repository.countTotalFiltrado(startDate, endDate, dni, nombre, departamento);
    }

    private AccTransactionDTO decorate(AccTransactionDTO dto) {
        String translated = EventDictionary.translate(dto.getEventName());

        return new AccTransactionDTO() {
            public String getId() { return dto.getId(); }
            public LocalDateTime getEventTime() { return dto.getEventTime(); }
            public String getEventName() { return dto.getEventName(); }
            public String getEventDescription() { return translated; }
            public String getDevAlias() { return dto.getDevAlias(); }
            public String getReaderName() { return dto.getReaderName(); }
            public String getVerifyModeName() { return dto.getVerifyModeName(); }
            public String getPersonPin() { return dto.getPersonPin(); }
            public String getPersonName() { return dto.getPersonName(); }
            public String getPersonLastName() { return dto.getPersonLastName(); }
            public String getDepartmentName() { return dto.getDepartmentName(); }
        };
    }


}
