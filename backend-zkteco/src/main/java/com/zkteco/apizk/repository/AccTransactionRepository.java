package com.zkteco.apizk.repository;

import com.zkteco.apizk.dto.AccTransactionDTO;
import com.zkteco.apizk.dto.AccesosPorDispositivoDTO;
import com.zkteco.apizk.dto.AccesosPorEventoDTO;

import com.zkteco.apizk.entity.AccTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccTransactionRepository
        extends JpaRepository<AccTransaction, String> {

    Page<AccTransaction> findAllByOrderByEventTimeDesc(Pageable pageable);

    Page<AccTransaction> findByEventTimeGreaterThanEqualOrderByEventTimeDesc(
            LocalDateTime startTime,
            Pageable pageable
    );

    Page<AccTransaction> findByEventTimeLessThanEqualOrderByEventTimeDesc(
            LocalDateTime endTime,
            Pageable pageable
    );

    Page<AccTransaction> findByEventTimeBetweenOrderByEventTimeDesc(
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    );

    /*CONSULTA PARA OBTENER LOS REGISTROS DE EVENTOS*/
    @Query(
        value = """
            SELECT
                t.id               AS id,
                t.event_time       AS eventTime,
                t.event_name       AS eventName,
                t.dev_alias        AS devAlias,
                t.reader_name      AS readerName,
                t.verify_mode_name AS verifyModeName,
                COALESCE(p.pin, t.pin) AS personPin,
                COALESCE(p.name, '') AS personName,
                COALESCE(p.last_name, '') AS personLastName,
                COALESCE(d.name, '') AS departmentName
            FROM acc_transaction t
            LEFT JOIN pers_person p ON p.pin = t.pin
            LEFT JOIN auth_department d ON d.id = p.auth_dept_id
            WHERE t.event_time BETWEEN CAST(:startDate AS timestamp)
                                AND CAST(:endDate   AS timestamp)
            ORDER BY t.event_time DESC
            """,
        countQuery = """
            SELECT COUNT(*)
            FROM acc_transaction t
            WHERE t.event_time BETWEEN CAST(:startDate AS timestamp)
                                AND CAST(:endDate   AS timestamp)
            """,
        nativeQuery = true
    )
    Page<AccTransactionDTO> findTransactionsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );


    /*CONSULTA PARA OBTENER LA CANTIDAD DE REGISTROS POR DISPOSITIVO*/
    @Query(
        value = """
            SELECT
                t.dev_alias,
                COUNT(*)::bigint
            FROM acc_transaction t
            WHERE t.event_time BETWEEN :startDate AND :endDate
            GROUP BY t.dev_alias
            ORDER BY COUNT(*) DESC
        """,
        nativeQuery = true
    )
    List<Object[]> countAccesosRaw(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /*CONSULTA PARA OBTENER EL TOTAL GENERAL DE REGISTROS*/ 
    @Query(
        value = """
            SELECT COUNT(*)::bigint
            FROM acc_transaction
            WHERE event_time BETWEEN :startDate AND :endDate
        """,
        nativeQuery = true
    )
    Long countTotalAccesos(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    /*CONSULTA PARA SABER LA CANTIDAD DE REGISTROS POR EVENTO*/
    @Query(
        value = """
            SELECT event_name, COUNT(*)::bigint
            FROM acc_transaction
            WHERE event_time BETWEEN :startDate AND :endDate
            GROUP BY event_name
            ORDER BY COUNT(*) DESC
        """,
        nativeQuery = true
    )
    List<Object[]> countByEventName(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    /*CONSULTA PARA SABER LA CANTIDAD DE REGISTROS POR HORA */
    @Query(
        value = """
            SELECT EXTRACT(HOUR FROM event_time)::int AS hora,
                COUNT(*)::bigint
            FROM acc_transaction
            WHERE event_time BETWEEN :startDate AND :endDate
            GROUP BY hora
            ORDER BY hora
        """,
        nativeQuery = true
    )
    List<Object[]> countByHour(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    /*CONSULTA PARA SABER LOS ULTIMOS ACCESOS*/
    @Query(
        value = """
            SELECT
                t.id               AS id,
                t.event_time       AS eventTime,
                t.event_name       AS eventName,
                t.dev_alias        AS devAlias,
                t.reader_name      AS readerName,
                t.verify_mode_name AS verifyModeName,
                COALESCE(p.pin, t.pin) AS personPin,
                COALESCE(p.name, '') AS personName,
                COALESCE(p.last_name, '') AS personLastName,
                COALESCE(d.name, '') AS departmentName
            FROM acc_transaction t
            LEFT JOIN pers_person p ON p.pin = t.pin
            LEFT JOIN auth_department d ON d.id = p.auth_dept_id
            ORDER BY t.event_time DESC
            LIMIT :limit
        """,
        nativeQuery = true
    )
    List<AccTransactionDTO> findLastTransactions(@Param("limit") int limit);

    /*CONSULTA PARA LISTAR LOS DISPOSITIVOS*/
    @Query(
        value = """
            SELECT DISTINCT dev_alias
            FROM acc_device
            WHERE dev_alias IS NOT NULL
            ORDER BY dev_alias
        """,
        nativeQuery = true
    )
    List<String> findDistinctDevices();


    /*CONSULTA PARA LISTAR LOS DEPARTAMENTOS*/
    @Query(
        value = """
            SELECT DISTINCT d.name
            FROM auth_department d
            ORDER BY d.name
        """,
        nativeQuery = true
    )
    List<String> findDepartments();





    /*SQL POTENTE CON FILTROS*/
    @Query(
        value = """
            SELECT
                t.id               AS id,
                t.event_time       AS eventTime,
                t.event_name       AS eventName,
                t.dev_alias        AS devAlias,
                t.reader_name      AS readerName,
                t.verify_mode_name AS verifyModeName,
                COALESCE(p.pin, t.pin) AS personPin,
                COALESCE(p.name, '') AS personName,
                COALESCE(p.last_name, '') AS personLastName,
                COALESCE(d.name, '') AS departmentName
            FROM acc_transaction t
            LEFT JOIN pers_person p ON p.pin = t.pin
            LEFT JOIN auth_department d ON d.id = p.auth_dept_id
            WHERE t.event_time BETWEEN :startDate AND :endDate
            
            AND (:dni IS NULL OR p.pin ILIKE CONCAT('%', :dni, '%'))
            AND (:nombre IS NULL OR (p.name || ' ' || p.last_name) ILIKE CONCAT('%', :nombre, '%'))
            AND (:departamento IS NULL OR d.name = :departamento)

            ORDER BY t.event_time DESC
            """,
        countQuery = """
            SELECT COUNT(*)
            FROM acc_transaction t
            LEFT JOIN pers_person p ON p.pin = t.pin
            LEFT JOIN auth_department d ON d.id = p.auth_dept_id
            WHERE t.event_time BETWEEN :startDate AND :endDate
            
            AND (:dni IS NULL OR p.pin ILIKE CONCAT('%', :dni, '%'))
            AND (:nombre IS NULL OR (p.name || ' ' || p.last_name) ILIKE CONCAT('%', :nombre, '%'))
            AND (:departamento IS NULL OR d.name = :departamento)
            """,
        nativeQuery = true
    )
    Page<AccTransactionDTO> findTransactionsFiltrado(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("dni") String dni,
        @Param("nombre") String nombre,
        @Param("departamento") String departamento,
        Pageable pageable
    );


    /*sql ayudante del de arribia*/
    @Query(
        value = """
            SELECT COUNT(*)
            FROM acc_transaction t
            LEFT JOIN pers_person p ON p.pin = t.pin
            LEFT JOIN auth_department d ON d.id = p.auth_dept_id
            WHERE t.event_time BETWEEN :startDate AND :endDate

            AND (:dni IS NULL OR p.pin ILIKE CONCAT('%', :dni, '%'))
            AND (:nombre IS NULL OR (p.name || ' ' || p.last_name) ILIKE CONCAT('%', :nombre, '%'))
            AND (:departamento IS NULL OR d.name = :departamento)
        """,
        nativeQuery = true
    )
    Long countTotalFiltrado(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("dni") String dni,
            @Param("nombre") String nombre,
            @Param("departamento") String departamento
    );




}


