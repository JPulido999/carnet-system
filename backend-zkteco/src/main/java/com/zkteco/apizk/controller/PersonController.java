package com.zkteco.apizk.controller;

import org.springframework.web.bind.annotation.*;
import com.zkteco.apizk.entity.Person;
import com.zkteco.apizk.service.PersonService;
import java.util.List;


@RestController
@RequestMapping("/api/zk/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Person> listar() {
        return service.listar();
    }
}

