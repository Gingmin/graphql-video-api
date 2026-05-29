package com.example.person.application;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.example.person.domain.Person;

public interface PersonRepository {
    PersonPage findPage(Integer page, Integer size);

    Optional<Person> findById(UUID id);

    Person addPerson(String code, LocalDate birthDate, String nationality);

    Person modifyPerson(UUID id, String code, LocalDate birthDate, String nationality);

    boolean deletePerson(UUID id);

    boolean existsByCode(String code);
}
