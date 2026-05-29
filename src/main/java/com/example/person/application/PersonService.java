package com.example.person.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import com.example.person.domain.Person;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PersonPage persons() {
        return persons(1, 20);
    }

    @Transactional(readOnly = true)
    public PersonPage persons(Integer page, Integer size) {
        int p = page == null ? 1 : page;
        int s = size == null ? 20 : size;
        if (p < 1) {
            throw new IllegalArgumentException("page must be greater than or equal to 1");
        }
        if (s < 1 || s > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return personRepository.findPage(p, s);
    }

    @Transactional(readOnly = true)
    public Person person(UUID id) {
        return personRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("person not found"));
    }

    @Transactional
    public Person addPerson(String code, LocalDate birthDate, String nationality) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (personRepository.existsByCode(code)) {
            throw new IllegalArgumentException("code already exists: " + code);
        }
        return personRepository.addPerson(code, birthDate, nationality);
    }

    @Transactional
    public Person modifyPerson(UUID id, String code, LocalDate birthDate, String nationality) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        return personRepository.modifyPerson(id, code, birthDate, nationality);
    }

    @Transactional
    public boolean deletePerson(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return personRepository.deletePerson(id);
    }
}
