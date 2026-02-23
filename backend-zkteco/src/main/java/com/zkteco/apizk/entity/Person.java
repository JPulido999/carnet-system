package com.zkteco.apizk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "pers_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    private String id;   // 👈 CLAVE DEL ARREGLO

    @Column(name = "pin")
    private String pin;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "status")
    private Integer status;

    @Column(name = "company_id")
    private String companyId; // ⚠️ también suele ser String en ZKTeco
}
