package com.zkteco.apizk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "acc_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccTransaction {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "dev_alias")
    private String devAlias;

    @Column(name = "dev_id")
    private String devId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "reader_name")
    private String readerName;

    @Column(name = "verify_mode_name")
    private String verifyModeName;

}