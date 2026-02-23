package com.zkteco.apizk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zkteco.apizk.entity.Person;
import com.zkteco.apizk.repository.PersonRepository;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository repo;

    public PersonService(PersonRepository repo) {
        this.repo = repo;
    }

    public List<Person> listar() {
        return repo.findAll();
    }
}

